package com.trading_platform.authentication;


import jakarta.annotation.Priority;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Priority(1)
public class RequireRoleAspect {
    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        RequestContext ctx = RequestContextHolder.get();
        if (ctx == null || !ctx.hasRole(requireRole.value())) {
            throw new RuntimeException("Access denied: missing role " + requireRole.value());
        }
    }
}
