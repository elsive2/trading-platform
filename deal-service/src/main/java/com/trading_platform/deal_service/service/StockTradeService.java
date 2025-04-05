package com.trading_platform.deal_service.service;

import com.trading_platform.deal_service.dto.request.StockTradeRequest;
import com.trading_platform.deal_service.dto.response.StockTradeResponse;
import com.trading_platform.deal_service.entity.User;
import com.trading_platform.deal_service.entity.PortfolioItem;
import com.trading_platform.deal_service.exception.UserNotFoundException;
import com.trading_platform.deal_service.exception.NotEnoughBalanceException;
import com.trading_platform.deal_service.exception.NotEnoughTicketAmountException;
import com.trading_platform.deal_service.repository.UserRepository;
import com.trading_platform.deal_service.repository.PortfolioItemRepository;
import com.trading_platform.deal_service.service.client.StockClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class StockTradeService {
    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    private final StockClient stockClient;

    public StockTradeService(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository, StockClient stockClient) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.stockClient = stockClient;
    }

    @Transactional
    public Mono<StockTradeResponse> trade(Mono<StockTradeRequest> stockTradeRequest) {
        return stockTradeRequest
                .flatMap(request -> stockClient.findById(request.getStockId())
                            .map(stockResponse -> stockResponse.getPrice() * request.getQuantity())
                            .flatMap(totalCost -> userRepository.findById(request.getUserId())
                                            .switchIfEmpty(Mono.error(new UserNotFoundException(request.getUserId())))
                                            .flatMap(user -> handleTradeAction(user, request, totalCost))
                                            .map(user -> buildTradeResponse(user, request, totalCost))
                            )
                );
    }

    private StockTradeResponse buildTradeResponse(User user, StockTradeRequest request, int totalCost) {
        return new StockTradeResponse(
                user.getId(),
                request.getTicker(),
                totalCost / request.getQuantity(),
                request.getQuantity(),
                request.getTradeAction(),
                totalCost,
                user.getBalance()
        );
    }

    private Mono<? extends User> handleTradeAction(User user, StockTradeRequest request, int totalCost) {
        switch (request.getTradeAction()) {
            case BUY -> {
                if (user.getBalance() - totalCost < 0) {
                    return Mono.error(new NotEnoughBalanceException());
                }

                return portfolioItemRepository.findByUserIdAndStockId(user.getId(), request.getStockId())
                        .flatMap(portfolioItem -> {
                            portfolioItem.setQuantity(portfolioItem.getQuantity() + request.getQuantity());
                            return portfolioItemRepository.save(portfolioItem);
                        })
                        .switchIfEmpty(portfolioItemRepository.save(new PortfolioItem(null, user.getId(), request.getStockId(), request.getQuantity())))
                        .flatMap(savedPortfolioItem -> {
                            user.setBalance(user.getBalance() - totalCost);
                            return userRepository.save(user);
                        });
            }

            case SELL -> {
                return portfolioItemRepository.findByUserIdAndStockId(user.getId(), request.getStockId())
                        .switchIfEmpty(Mono.error(new NotEnoughTicketAmountException()))
                        .filter(portfolioItem -> portfolioItem.getQuantity() >= request.getQuantity())
                        .switchIfEmpty(Mono.error(new NotEnoughTicketAmountException()))
                        .flatMap(portfolioItem -> {
                            portfolioItem.setQuantity(portfolioItem.getQuantity() - request.getQuantity());
                            return portfolioItemRepository.save(portfolioItem);
                        })
                        .flatMap(savedPortfolioItem -> {
                            user.setBalance(user.getBalance() + totalCost);
                            return userRepository.save(user);
                        });
            }
        }
        return Mono.empty();
    }
}
