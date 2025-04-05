package com.trading_platform.api_gateway.filter;

import com.trading_platform.api_gateway.service.AuthService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final AuthService authService;

    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (Objects.isNull(authorization) || !authorization.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        return Mono.just(authorization)
                .flatMap(token -> authService.auth(token)
                            .map(authResponse -> exchange.getRequest()
                                .mutate()
                                .header("X-User-ID", String.valueOf(authResponse.id()))
                                .header("X-User-Roles", String.join(",", authResponse.roles()))
                                .build()
                            )
                            .doOnNext(System.out::println)
                            .flatMap(request -> chain.filter(exchange.mutate().request(request).build()))
                );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
