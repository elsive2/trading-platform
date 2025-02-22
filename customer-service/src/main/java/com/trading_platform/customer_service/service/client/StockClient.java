package com.trading_platform.customer_service.service.client;

import com.trading_platform.customer_service.dto.response.StockResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
