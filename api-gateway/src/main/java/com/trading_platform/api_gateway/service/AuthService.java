package com.trading_platform.api_gateway.service;

import com.trading_platform.api_gateway.dto.resonse.AuthResponseDto;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final WebClient webClient;

    public AuthService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://authorization-service")
                .build();
    }

    public Mono<AuthResponseDto> auth(String token) {
        return webClient.get()
                .uri("/me")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(AuthResponseDto.class);
    }
}
