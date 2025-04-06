package com.trading_platform.deal_service.repository;

import com.trading_platform.deal_service.entity.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Integer> {
}
