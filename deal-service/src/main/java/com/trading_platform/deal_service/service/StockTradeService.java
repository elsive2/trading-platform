package com.trading_platform.deal_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading_platform.deal_service.dto.StockTradeEvent;
import com.trading_platform.deal_service.dto.request.StockTradeRequest;
import com.trading_platform.deal_service.dto.response.StockTradeResponse;
import com.trading_platform.deal_service.entity.Account;
import com.trading_platform.deal_service.entity.Outbox;
import com.trading_platform.deal_service.entity.PortfolioItem;
import com.trading_platform.deal_service.exception.AccountNotFoundException;
import com.trading_platform.deal_service.exception.NotEnoughBalanceException;
import com.trading_platform.deal_service.exception.NotEnoughTicketAmountException;
import com.trading_platform.deal_service.repository.AccountRepository;
import com.trading_platform.deal_service.repository.OutboxRepository;
import com.trading_platform.deal_service.repository.PortfolioItemRepository;
import com.trading_platform.deal_service.service.client.StockClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StockTradeService {
    private final AccountRepository accountRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    private final StockClient stockClient;
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    @Transactional
    public Mono<StockTradeResponse> trade(Mono<StockTradeRequest> stockTradeRequest, String userId) {
        return stockTradeRequest
                .flatMap(request ->
                        stockClient.findById(request.getStockId())
                                .map(stockResponse -> stockResponse.getPrice() * request.getQuantity())
                                .flatMap(totalCost ->
                                        accountRepository.findByUserId(userId)
                                                .switchIfEmpty(Mono.error(new AccountNotFoundException(userId)))
                                                .flatMap(account -> handleTradeAction(account, request, totalCost)
                                                        .flatMap(updatedAccount -> {
                                                            String payload;
                                                            try {
                                                                payload = objectMapper.writeValueAsString(
                                                                        new StockTradeEvent(request.getStockId(), totalCost, request.getTradeAction())
                                                                );
                                                            } catch (JsonProcessingException e) {
                                                                return Mono.error(new RuntimeException("Serialization failed", e));
                                                            }

                                                            Outbox outbox = new Outbox(UUID.randomUUID(), payload, Instant.now());

                                                            return outboxRepository.save(outbox)
                                                                    .thenReturn(updatedAccount);
                                                        })
                                                )
                                                .map(account -> buildTradeResponse(account, request, totalCost))
                                )
                );
    }


    private StockTradeResponse buildTradeResponse(Account account, StockTradeRequest request, int totalCost) {
        return new StockTradeResponse(
                account.getId(),
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
