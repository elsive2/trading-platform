package com.trading_platform.config;

import com.trading_platform.authentication.RequestContextWebFilter;
import com.trading_platform.authentication.RequireRoleAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableConfigurationProperties(HeaderProperties.class)
public class RequestContextAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebFilter requestContextFilter(HeaderProperties headerProperties) {
        return new RequestContextWebFilter(headerProperties);
    }

    @Bean
    public RequireRoleAspect requireRoleAspect() {
        return new RequireRoleAspect();
    }
}
