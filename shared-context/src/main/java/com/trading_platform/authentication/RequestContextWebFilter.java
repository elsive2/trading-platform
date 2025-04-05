package com.trading_platform.authentication;

import com.trading_platform.config.HeaderProperties;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Order(-100)
public class RequestContextWebFilter implements WebFilter {

    private final HeaderProperties headerProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var request = exchange.getRequest();
        var headers = request.getHeaders();

        String userId = headers.getFirst(headerProperties.getUserId());
        String rolesHeader = headers.getFirst(headerProperties.getUserRoles());

        Set<String> roles = rolesHeader != null
                ? Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet())
                : Set.of();

        RequestContextHolder.set(new RequestContext(userId, roles));

        return chain.filter(exchange)
                .doFinally(signalType -> RequestContextHolder.clear());
    }
}