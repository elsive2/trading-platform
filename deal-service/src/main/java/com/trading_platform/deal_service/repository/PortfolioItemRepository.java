package com.trading_platform.deal_service.repository;

import com.trading_platform.deal_service.entity.PortfolioItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
    Mono<PortfolioItem> findByAccountIdAndStockId(Integer accountId, Integer stockId);

    Flux<PortfolioItem> findByAccountId(Integer accountId);
}
