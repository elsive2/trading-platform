package com.trading_platform.authentication;

import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestContextAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Filter requestContextFilter() {
        return new RequestContextFilter();
    }
}
