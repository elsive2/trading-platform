package com.trading_platform.config;

import com.trading_platform.authentication.RequestContextWebFilter;
import com.trading_platform.authentication.aspect.AuthorizedAspect;
import com.trading_platform.authentication.aspect.AuthorizedClassAspect;
import com.trading_platform.authentication.aspect.RequireRoleAspect;
import com.trading_platform.authentication.aspect.RequireRoleClassAspect;
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

    @Bean
    public RequireRoleClassAspect requireRoleClassAspect() {
        return new RequireRoleClassAspect();
    }

    @Bean
    public AuthorizedAspect authorizedAspect() {
        return new AuthorizedAspect();
    }

    @Bean
    public AuthorizedClassAspect authorizedClassAspect() {
        return new AuthorizedClassAspect();
    }
}
