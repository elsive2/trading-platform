package com.trading_platform.deal_service.repository;

import com.trading_platform.deal_service.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
}
