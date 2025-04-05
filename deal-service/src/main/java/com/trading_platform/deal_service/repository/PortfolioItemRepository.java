package com.trading_platform.deal_service.repository;

import com.trading_platform.deal_service.entity.PortfolioItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
    Mono<PortfolioItem> findByUserIdAndStockId(Integer userId, Integer stockId);

    Flux<PortfolioItem> findByUserId(Integer userId);
}
