package com.trading_platform.authorization_service.filter;

import com.trading_platform.authorization_service.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Component
public class JwtFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public @NotNull Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        List<String> authorizationHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (authorizationHeaders == null || authorizationHeaders.isEmpty()) {
            return chain.filter(exchange);
        }

        String authHeader = authorizationHeaders.getFirst();

        String START_AUTHORIZATION_HEADER = "Bearer ";
        if (!authHeader.startsWith(START_AUTHORIZATION_HEADER)) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(START_AUTHORIZATION_HEADER.length());

        return Mono.just(token)
                .filter(jwtUtil::validateToken)
                .map(validToken -> {
                    String username = jwtUtil.getUsernameFromToken(validToken);
                    Claims claims = jwtUtil.getAllClaimsFromToken(validToken);
                    List<String> roles = claims.get("role", List.class);
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            roles.stream().map(SimpleGrantedAuthority::new).toList()
                    );
                })
                .flatMap(authentication -> {
                    SecurityContext context = new SecurityContextImpl(authentication);
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
