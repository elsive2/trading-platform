package com.trading_platform.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class RequestContext {
    private final String userId;
    private final Set<String> roles;

    public boolean hasRole(String value) {
        return roles.contains(value);
    }
}
