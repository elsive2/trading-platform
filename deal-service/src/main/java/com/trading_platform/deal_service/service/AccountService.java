package com.trading_platform.deal_service.service;

import com.trading_platform.deal_service.dto.AccountInformationResponse;
import com.trading_platform.deal_service.entity.Account;
import com.trading_platform.deal_service.exception.AccountNotFoundException;
import com.trading_platform.deal_service.repository.AccountRepository;
import com.trading_platform.deal_service.repository.PortfolioItemRepository;
import com.trading_platform.deal_service.dto.Holding;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public AccountService(AccountRepository accountRepository, PortfolioItemRepository portfolioItemRepository) {
        this.accountRepository = accountRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public Mono<AccountInformationResponse> findOrCreateByUserId(String userId) {
        return accountRepository.findByUserId(userId)
                .switchIfEmpty(accountRepository.save(new Account(null, userId, 0)))
                .flatMap(account -> portfolioItemRepository.findByAccountId(account.getId())
                        .map(portfolioItem -> new Holding(portfolioItem.getAccountId(), portfolioItem.getQuantity()))
                        .collectList()
                        .map(holdings -> new AccountInformationResponse(account.getId(), account.getBalance(), holdings))
                );
    }
}
