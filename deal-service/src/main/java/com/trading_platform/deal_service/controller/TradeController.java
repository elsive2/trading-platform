package com.trading_platform.deal_service.controller;

import com.trading_platform.authentication.RequireRole;
import com.trading_platform.deal_service.dto.request.StockTradeRequest;
import com.trading_platform.deal_service.dto.response.StockTradeResponse;
import com.trading_platform.deal_service.service.StockTradeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

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

    @RequireRole("ROLE_ADMIN")
    @GetMapping("/test")
    public Mono<?> test(ServerHttpRequest request) {
        return Mono.just(getHeaders(request));
    }

    @GetMapping
    public Map<String, String> getHeaders(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        return headers.toSingleValueMap(); // Возвращает первый заголовок, если их несколько
    }
}
