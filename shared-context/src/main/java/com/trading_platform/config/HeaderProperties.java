package com.trading_platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "app.headers")
public class HeaderProperties {
    private String userId;
    private String userRoles;
}
