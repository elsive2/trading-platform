package com.trading_platform.deal_service.controller;

import com.trading_platform.deal_service.dto.request.StockTradeRequest;
import com.trading_platform.deal_service.dto.response.StockTradeResponse;
import com.trading_platform.deal_service.service.StockTradeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stocks")
public class TradeController {
    private final StockTradeService stockTradeService;

    public TradeController(StockTradeService stockTradeService) {
        this.stockTradeService = stockTradeService;
    }

    @PostMapping("/trade")
    public Mono<StockTradeResponse> trade(@RequestBody Mono<StockTradeRequest> stockTradeRequest) {
        return stockTradeService.trade(stockTradeRequest);
    }

    @GetMapping("/test")
    public Mono<String> trade() {
        return Mono.just("sads");
    }
}
