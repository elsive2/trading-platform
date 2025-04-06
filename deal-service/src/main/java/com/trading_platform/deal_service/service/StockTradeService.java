package com.trading_platform.deal_service.service;

import com.trading_platform.deal_service.dto.request.StockTradeRequest;
import com.trading_platform.deal_service.dto.response.StockTradeResponse;
import com.trading_platform.deal_service.entity.Account;
import com.trading_platform.deal_service.entity.PortfolioItem;
import com.trading_platform.deal_service.exception.AccountNotFoundException;
import com.trading_platform.deal_service.exception.NotEnoughBalanceException;
import com.trading_platform.deal_service.exception.NotEnoughTicketAmountException;
import com.trading_platform.deal_service.repository.AccountRepository;
import com.trading_platform.deal_service.repository.PortfolioItemRepository;
import com.trading_platform.deal_service.service.client.StockClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class StockTradeService {
    private final AccountRepository accountRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    private final StockClient stockClient;

    public StockTradeService(AccountRepository accountRepository, PortfolioItemRepository portfolioItemRepository, StockClient stockClient) {
        this.accountRepository = accountRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.stockClient = stockClient;
    }

    @Transactional
    public Mono<StockTradeResponse> trade(Mono<StockTradeRequest> stockTradeRequest) {
        return stockTradeRequest
                .flatMap(request -> stockClient.findById(request.getStockId())
                            .map(stockResponse -> stockResponse.getPrice() * request.getQuantity())
                            .flatMap(totalCost -> accountRepository.findById(request.getAccountId())
                                            .switchIfEmpty(Mono.error(new AccountNotFoundException(request.getAccountId())))
                                            .flatMap(account -> handleTradeAction(account, request, totalCost))
                                            .map(account -> buildTradeResponse(account, request, totalCost))
                            )
                );
    }

    private StockTradeResponse buildTradeResponse(Account account, StockTradeRequest request, int totalCost) {
        return new StockTradeResponse(
                account.getId(),
                request.getTicker(),
                totalCost / request.getQuantity(),
                request.getQuantity(),
                request.getTradeAction(),
                totalCost,
                account.getBalance()
        );
    }

    private Mono<? extends Account> handleTradeAction(Account account, StockTradeRequest request, int totalCost) {
        switch (request.getTradeAction()) {
            case BUY -> {
                if (account.getBalance() - totalCost < 0) {
                    return Mono.error(new NotEnoughBalanceException());
                }

                return portfolioItemRepository.findByAccountIdAndStockId(account.getId(), request.getStockId())
                        .flatMap(portfolioItem -> {
                            portfolioItem.setQuantity(portfolioItem.getQuantity() + request.getQuantity());
                            return portfolioItemRepository.save(portfolioItem);
                        })
                        .switchIfEmpty(portfolioItemRepository.save(new PortfolioItem(null, account.getId(), request.getStockId(), request.getQuantity())))
                        .flatMap(savedPortfolioItem -> {
                            account.setBalance(account.getBalance() - totalCost);
                            return accountRepository.save(account);
                        });
            }

            case SELL -> {
                return portfolioItemRepository.findByAccountIdAndStockId(account.getId(), request.getStockId())
                        .switchIfEmpty(Mono.error(new NotEnoughTicketAmountException()))
                        .filter(portfolioItem -> portfolioItem.getQuantity() >= request.getQuantity())
                        .switchIfEmpty(Mono.error(new NotEnoughTicketAmountException()))
                        .flatMap(portfolioItem -> {
                            portfolioItem.setQuantity(portfolioItem.getQuantity() - request.getQuantity());
                            return portfolioItemRepository.save(portfolioItem);
                        })
                        .flatMap(savedPortfolioItem -> {
                            account.setBalance(account.getBalance() + totalCost);
                            return accountRepository.save(account);
                        });
            }
        }
        return Mono.empty();
    }
}
