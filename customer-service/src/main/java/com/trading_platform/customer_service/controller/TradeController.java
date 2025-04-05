package com.trading_platform.customer_service.controller;

import com.trading_platform.authentication.RequireRole;
import com.trading_platform.customer_service.dto.request.StockTradeRequest;
import com.trading_platform.customer_service.dto.response.StockTradeResponse;
import com.trading_platform.customer_service.service.StockTradeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
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
