package com.trading_platform.stock_service.controller;

import com.trading_platform.stock_service.dto.request.StockCreateRequestDto;
import com.trading_platform.stock_service.dto.response.StockResponseDto;
import com.trading_platform.stock_service.service.StockService;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<List<StockResponseDto>>> streamPrices(
            @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {
        return stockService.activeStream(page, size);
    }

    @GetMapping(value = "/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockResponseDto> getStockUpdates(
            @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {
        return stockService.getStockUpdates(page, size);
    }

    @GetMapping
    public Flux<StockResponseDto> findAll(
            @RequestParam(value = "page", defaultValue = "1") @Min(1) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {
        return stockService.findAll(page, size);
    }

    @GetMapping("/{id}")
    public Mono<StockResponseDto> findById(@PathVariable @Min(1) Integer id) {
        return stockService.findById(id);
    }

    @PostMapping
    public Mono<StockResponseDto> create(@RequestBody Mono<StockCreateRequestDto> requestDto) {
        return stockService.create(requestDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<StockResponseDto> update(@PathVariable @Min(1) Integer id, @RequestBody Mono<StockCreateRequestDto> requestDto) {
        return stockService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable @Min(1) Integer id) {
        return stockService.delete(id);
    }
}
