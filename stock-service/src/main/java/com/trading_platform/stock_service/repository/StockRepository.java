package com.trading_platform.stock_service.repository;

import com.trading_platform.stock_service.entity.Stock;
import com.trading_platform.stock_service.enums.TickerEnum;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface StockRepository extends R2dbcRepository<Stock, Integer> {
    Mono<Stock> findByName(TickerEnum tickerEnum);
}
