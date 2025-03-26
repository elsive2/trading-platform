package com.trading_platform.api_gateway.filter;

import com.trading_platform.api_gateway.service.AuthService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtAuthenticationFilter implements GatewayFilter {
    private final AuthService authService;

    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.just(String.valueOf(exchange.getRequest().getHeaders().get("Authorization")))
                .flatMap(token -> {
                    if (Objects.isNull(token) || !token.startsWith("Bearer ")) {
                        return chain.filter(exchange);
                    }
                    return authService.auth(token)
                            .map(authResponse -> exchange.getRequest()
                                    .mutate()
                                    .header("X-User-ID", String.valueOf(authResponse.id()))
                                    .header("X-User-Role", String.valueOf(authResponse.roles()))
                                    .build()
                            )
                            .flatMap(request -> chain.filter(exchange));
                });
    }
}
