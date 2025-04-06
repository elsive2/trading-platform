package com.trading_platform.deal_service.service.client;

import com.trading_platform.deal_service.dto.response.StockResponse;
import com.trading_platform.exception.StockNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StockClient {
    private final WebClient webClient;

    public StockClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://stock-service")
                .build();
    }

    public Mono<StockResponse> findById(Integer id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        clientResponse -> Mono.error(new StockNotFoundException(id))
                )
                .bodyToMono(StockResponse.class);
    }
}
