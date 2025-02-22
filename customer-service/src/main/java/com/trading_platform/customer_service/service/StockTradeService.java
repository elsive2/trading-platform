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
import com.trading_platform.customer_service.service.client.StockClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class StockTradeService {
    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    private final StockClient stockClient;

    public StockTradeService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository, StockClient stockClient) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.stockClient = stockClient;
    }

    @Transactional
    public Mono<StockTradeResponse> trade(Mono<StockTradeRequest> stockTradeRequest) {
        return stockTradeRequest
                .flatMap(request -> stockClient.findById(request.getStockId())
                            .map(stockResponse -> stockResponse.getPrice() * request.getQuantity())
                            .flatMap(totalCost -> customerRepository.findById(request.getCustomerId())
                                            .switchIfEmpty(Mono.error(new CustomerNotFoundException(request.getCustomerId())))
                                            .flatMap(customer -> handleTradeAction(customer, request, totalCost))
                                            .map(customer -> buildTradeResponse(customer, request, totalCost))
                            )
                );
    }

    private StockTradeResponse buildTradeResponse(Customer customer, StockTradeRequest request, int totalCost) {
        return new StockTradeResponse(
                customer.getId(),
                request.getTicker(),
                totalCost / request.getQuantity(),
                request.getQuantity(),
                request.getTradeAction(),
                totalCost,
                customer.getBalance()
        );
    }

    private Mono<? extends Customer> handleTradeAction(Customer customer, StockTradeRequest request, int totalCost) {
        switch (request.getTradeAction()) {
            case BUY -> {
                if (customer.getBalance() - totalCost < 0) {
                    return Mono.error(new NotEnoughBalanceException());
                }

                return portfolioItemRepository.findByCustomerIdAndStockId(customer.getId(), request.getStockId())
                        .flatMap(portfolioItem -> {
                            portfolioItem.setQuantity(portfolioItem.getQuantity() + request.getQuantity());
                            return portfolioItemRepository.save(portfolioItem);
                        })
                        .switchIfEmpty(portfolioItemRepository.save(new PortfolioItem(null, customer.getId(), request.getStockId(), request.getQuantity())))
                        .flatMap(savedPortfolioItem -> {
                            customer.setBalance(customer.getBalance() - totalCost);
                            return customerRepository.save(customer);
                        });
            }

            case SELL -> {
                return portfolioItemRepository.findByCustomerIdAndStockId(customer.getId(), request.getStockId())
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
