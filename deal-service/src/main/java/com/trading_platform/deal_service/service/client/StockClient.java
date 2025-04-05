package com.trading_platform.deal_service.service.client;

import com.trading_platform.deal_service.dto.response.StockResponse;
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
                .uri("/stocks/{id}", id)
                .retrieve()
                .bodyToMono(StockResponse.class);
    }
}
