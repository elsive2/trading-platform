package com.trading_platform.customer_service.repository;

import com.trading_platform.customer_service.entity.PortfolioItem;
import com.trading_platform.customer_service.enums.TickerEnum;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
    Mono<PortfolioItem> findByCustomerIdAndTicker(Integer customerId, TickerEnum ticker);

    Flux<PortfolioItem> findByCustomerId(Integer customerId);
}
