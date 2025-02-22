package com.trading_platform.stock_service.repository;

import com.trading_platform.stock_service.entity.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockRepository extends R2dbcRepository<Stock, Integer> {
    Mono<Stock> findByName(String tickerEnum);

    Flux<Stock> findBy(Pageable pageable);
}
