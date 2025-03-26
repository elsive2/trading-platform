package com.trading_platform.api_gateway.config;

import com.trading_platform.api_gateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/customers/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("backendService"))
                        )
                        .uri("lb://customer-service"))
                .route(r -> r.path("/stocks/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("backendService"))
                        )
                        .uri("lb://stock-service"))
                .build();
    }
}
