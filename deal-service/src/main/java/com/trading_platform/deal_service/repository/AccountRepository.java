package com.trading_platform.deal_service.repository;

import com.trading_platform.deal_service.dto.AccountInformationResponse;
import com.trading_platform.deal_service.entity.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Integer> {
    Mono<Account> findByUserId(String userId);
}
