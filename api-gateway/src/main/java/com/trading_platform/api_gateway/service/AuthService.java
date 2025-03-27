package com.trading_platform.api_gateway.service;

import com.trading_platform.api_gateway.dto.resonse.AuthResponseDto;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final WebClient webClient;

    public AuthService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Mono<AuthResponseDto> auth(String token) {
        return webClient.get()
                .uri("http://authorization-service/auth/me")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(AuthResponseDto.class);
    }
}
