package com.trading_platform.authentication.aspect;


import com.trading_platform.authentication.RequestContext;
import com.trading_platform.authentication.RequestContextHolder;
import com.trading_platform.authentication.RequireRole;
import com.trading_platform.exception.ForbiddenException;
import jakarta.annotation.Priority;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Objects;

@Aspect
@Priority(1)
public class RequireRoleAspect {
    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        RequestContext ctx = RequestContextHolder.get();
        if (Objects.isNull(ctx) || !ctx.hasRole(requireRole.value())) {
            throw new ForbiddenException(requireRole.value());
        }
    }
}
