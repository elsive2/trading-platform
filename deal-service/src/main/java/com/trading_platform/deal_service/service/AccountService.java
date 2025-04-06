package com.trading_platform.deal_service.service;

import com.trading_platform.deal_service.dto.AccountInformationResponse;
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

    public Mono<AccountInformationResponse> findById(Integer id) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(new AccountNotFoundException(id)))
                .flatMap(account -> portfolioItemRepository.findByAccountId(account.getId())
                            .map(portfolioItem -> new Holding(portfolioItem.getAccountId(), portfolioItem.getQuantity()))
                            .collectList()
                            .map(holdings -> new AccountInformationResponse(account.getId(), account.getUserId(), account.getBalance(), holdings))
                );
    }
}
