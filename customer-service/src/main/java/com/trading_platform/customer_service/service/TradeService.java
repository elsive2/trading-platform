package com.trading_platform.customer_service.service;

import com.trading_platform.customer_service.dto.request.StockTradeRequest;
import com.trading_platform.customer_service.dto.response.StockTradeResponse;
import com.trading_platform.customer_service.entity.Customer;
import com.trading_platform.customer_service.entity.PortfolioItem;
import com.trading_platform.customer_service.exception.CustomerNotFoundException;
import com.trading_platform.customer_service.exception.NotEnoughBalanceException;
import com.trading_platform.customer_service.exception.NotEnoughTicketAmountException;
import com.trading_platform.customer_service.repository.CustomerRepository;
import com.trading_platform.customer_service.repository.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class TradeService {
    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public TradeService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Transactional
    public Mono<StockTradeResponse> trade(Mono<StockTradeRequest> stockTradeRequest) {
        return stockTradeRequest
                .flatMap(request -> customerRepository.findById(request.getCustomerId())
                        .switchIfEmpty(Mono.error(new CustomerNotFoundException(request.getCustomerId())))
                        .flatMap(customer -> handleTradeAction(customer, request))
                        .flatMap(customer -> savePortfolioItem(customer, request))
                        .map(customer -> buildTradeResponse(customer, request))
                );
    }

    private StockTradeResponse buildTradeResponse(Customer customer, StockTradeRequest request) {
        int tradeAmount = request.getPrice() * request.getQuantity();
        return new StockTradeResponse(
                customer.getId(),
                request.getTicker(),
                request.getPrice(),
                request.getQuantity(),
                request.getTradeAction(),
                tradeAmount,
                customer.getBalance()
        );
    }

    private Mono<? extends Customer> savePortfolioItem(Customer customer, StockTradeRequest request) {
        PortfolioItem portfolioItem = new PortfolioItem();
        portfolioItem.setCustomerId(customer.getId());
        portfolioItem.setQuantity(request.getQuantity());
        portfolioItem.setTicker(request.getTicker());
        return portfolioItemRepository.save(portfolioItem).thenReturn(customer);
    }

    private Mono<? extends Customer> handleTradeAction(Customer customer, StockTradeRequest request) {
        int totalCost = request.getPrice() * request.getQuantity();
        switch (request.getTradeAction()) {
            case BUY -> {
                if (customer.getBalance() - totalCost < 0) {
                    return Mono.error(new NotEnoughBalanceException());
                }

                return portfolioItemRepository.findByCustomerIdAndTicker(customer.getId(), request.getTicker())
                        .switchIfEmpty(Mono.just(new PortfolioItem(null, customer.getId(), request.getTicker(), 0)))
                        .flatMap(portfolioItem -> {
                            portfolioItem.setQuantity(portfolioItem.getQuantity() + request.getQuantity());
                            return portfolioItemRepository.save(portfolioItem);
                        })
                        .flatMap(savedPortfolioItem -> {
                            customer.setBalance(customer.getBalance() - totalCost);
                            return customerRepository.save(customer);
                        });
            }

            case SELL -> {
                return portfolioItemRepository.findByCustomerIdAndTicker(customer.getId(), request.getTicker())
                        .switchIfEmpty(Mono.error(new NotEnoughTicketAmountException()))
                        .filter(portfolioItem -> portfolioItem.getQuantity() >= request.getQuantity())
                        .switchIfEmpty(Mono.error(new NotEnoughTicketAmountException()))
                        .flatMap(portfolioItem -> {
                            portfolioItem.setQuantity(portfolioItem.getQuantity() - request.getQuantity());
                            return portfolioItemRepository.save(portfolioItem);
                        })
                        .flatMap(savedPortfolioItem -> {
                            customer.setBalance(customer.getBalance() + totalCost);
                            return customerRepository.save(customer);
                        });
            }
        }
        return Mono.empty();
    }
}
