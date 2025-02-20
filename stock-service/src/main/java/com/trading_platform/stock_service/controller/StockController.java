package com.trading_platform.stock_service.controller;

import com.trading_platform.stock_service.dto.PriceResultDto;
import com.trading_platform.stock_service.entity.Stock;
import com.trading_platform.stock_service.enums.TickerEnum;
import com.trading_platform.stock_service.repository.StockRepository;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
public class StockController {
    private final StockRepository stockRepository;

    public StockController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping("/stream")
    public Flux<ServerSentEvent<List<PriceResultDto>>> streamPrices() {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(i -> getPrices().collectList())
                .map(prices -> ServerSentEvent.<List<PriceResultDto>>builder()
                        .event("ticker")
                        .data(prices)
                        .build());
    }

    private Flux<PriceResultDto> getPrices() {
        return Flux.just(TickerEnum.values())
                .flatMap(ticker -> stockRepository.findByName(ticker)
                            .switchIfEmpty(stockRepository.save(new Stock(null, ticker, (int) (Math.random() * 100))))
                )
                .map(stock -> new PriceResultDto(stock.getName(), stock.getPrice()));
    }
}
