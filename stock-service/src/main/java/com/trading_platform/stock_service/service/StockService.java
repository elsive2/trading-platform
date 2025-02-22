package com.trading_platform.stock_service.service;

import com.trading_platform.stock_service.dto.request.StockCreateRequestDto;
import com.trading_platform.stock_service.dto.response.StockResponseDto;
import com.trading_platform.stock_service.entity.Stock;
import com.trading_platform.stock_service.exception.StockAlreadyExistsException;
import com.trading_platform.stock_service.exception.StockNotFoundException;
import com.trading_platform.stock_service.mapper.StockMapper;
import com.trading_platform.stock_service.repository.StockRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
public class StockService {
    private final StockRepository stockRepository;

    private final StockMapper mapper;

    private final Sinks.Many<StockResponseDto> sinks = Sinks.many().multicast().onBackpressureBuffer();

    public StockService(StockRepository stockRepository, StockMapper mapper) {
        this.stockRepository = stockRepository;
        this.mapper = mapper;
    }

    public Flux<StockResponseDto> findAll(Integer page, Integer size) {
        return stockRepository.findBy(PageRequest.of(page - 1, size))
                .map(mapper::toResponse);
    }

    public Mono<StockResponseDto> findById(Integer id) {
        return stockRepository.findById(id)
                .switchIfEmpty(Mono.error(new StockNotFoundException(id)))
                .map(mapper::toResponse);
    }

    public Mono<StockResponseDto> create(Mono<StockCreateRequestDto> requestDto) {
        return requestDto.flatMap(request ->
                stockRepository.findByName(request.getName())
                                .hasElement()
                                .flatMap(isExist -> {
                                    if (isExist) {
                                        return Mono.error(new StockAlreadyExistsException(request.getName()));
                                    }
                                    return stockRepository.save(new Stock(null, request.getName(), request.getPrice()))
                                            .map(mapper::toResponse)
                                            .doOnNext(sinks::tryEmitNext);
                                })
        );
    }

    public Mono<StockResponseDto> update(Integer id, Mono<StockCreateRequestDto> requestDto) {
        return requestDto
                .defaultIfEmpty(new StockCreateRequestDto())
                .flatMap(request -> stockRepository.findById(id)
                        .switchIfEmpty(Mono.error(new StockNotFoundException(id)))
                        .flatMap(existingStock -> {
                            if (Objects.nonNull(request.getName())) {
                                existingStock.setName(request.getName());
                            }
                            if (Objects.nonNull(request.getPrice())) {
                                existingStock.setPrice(request.getPrice());
                            }
                            return stockRepository.save(existingStock);
                        })
                        .map(mapper::toResponse)
                        .doOnNext(sinks::tryEmitNext)
                );
    }

    public Mono<Void> delete(Integer id) {
        return stockRepository.findById(id)
                .switchIfEmpty(Mono.error(new StockNotFoundException(id)))
                .flatMap(stockRepository::delete)
                .then();
    }

    public Flux<ServerSentEvent<List<StockResponseDto>>> activeStream(Integer page, Integer size) {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(i -> findAll(page, size).collectList())
                .map(prices -> ServerSentEvent.<List<StockResponseDto>>builder()
                        .data(prices)
                        .build());
    }

    public Flux<StockResponseDto> getStockUpdates(Integer page, Integer size) {
        final int min = (page - 1) * size;
        final int max = min + size;

        return sinks.asFlux().filter(prices -> prices.getId() >= min && prices.getId() < max);
    }
}
