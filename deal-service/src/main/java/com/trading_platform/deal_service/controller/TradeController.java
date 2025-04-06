package com.trading_platform.deal_service.controller;

import com.trading_platform.authentication.Authorized;
import com.trading_platform.authentication.RequestContext;
import com.trading_platform.authentication.RequestContextHolder;
import com.trading_platform.deal_service.dto.request.StockTradeRequest;
import com.trading_platform.deal_service.dto.response.StockTradeResponse;
import com.trading_platform.deal_service.service.StockTradeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Authorized
@RestController
@RequestMapping("/stocks")
public class TradeController {
    private final StockTradeService stockTradeService;

    public TradeController(StockTradeService stockTradeService) {
        this.stockTradeService = stockTradeService;
    }

    @PostMapping("/trade")
    public Mono<StockTradeResponse> trade(@RequestBody Mono<StockTradeRequest> stockTradeRequest) {
        RequestContext requestContext = RequestContextHolder.get();

        return stockTradeService.trade(stockTradeRequest, requestContext.getUserId());
    };
}
