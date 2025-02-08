package com.trading_platform.customer_service.controller;

import com.trading_platform.customer_service.dto.request.StockTradeRequest;
import com.trading_platform.customer_service.dto.response.StockTradeResponse;
import com.trading_platform.customer_service.service.StockTradeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TradeController {
    private final StockTradeService stockTradeService;

    public TradeController(StockTradeService stockTradeService) {
        this.stockTradeService = stockTradeService;
    }

    @PostMapping("trade")
    public Mono<StockTradeResponse> trade(@RequestBody Mono<StockTradeRequest> stockTradeRequest) {
        return stockTradeService.trade(stockTradeRequest);
    }
}
