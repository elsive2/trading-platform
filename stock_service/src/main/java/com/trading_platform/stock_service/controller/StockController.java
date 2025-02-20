package com.trading_platform.stock_service.controller;

import com.trading_platform.stock_service.dto.PriceResultDto;
import com.trading_platform.stock_service.enums.TickerEnum;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StockController {
    @GetMapping("/stream")
    public Flux<ServerSentEvent<List<PriceResultDto>>> streamPrices() {
        return Flux.interval(Duration.ofMinutes(1))
                .map(i -> ServerSentEvent.<List<PriceResultDto>>builder()
                        .id(String.valueOf(i))
                        .event("ticker")
                        .data(getPrices())
                        .build());
    }

    private static List<PriceResultDto> getPrices() {
        List<PriceResultDto> priceResult = new ArrayList<>();
        for (TickerEnum ticker : TickerEnum.values()) {
            priceResult.add(new PriceResultDto(ticker, (int) (Math.random() * 1000)));
        }
        return priceResult;
    }
}
