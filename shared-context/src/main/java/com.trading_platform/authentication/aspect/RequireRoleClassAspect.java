package com.trading_platform.authentication.aspect;

import com.trading_platform.authentication.RequestContext;
import com.trading_platform.authentication.RequestContextHolder;
import com.trading_platform.authentication.RequireRole;
import com.trading_platform.exception.ForbiddenException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Objects;

@Aspect
public class RequireRoleClassAspect {
    @Before("within(@com.trading_platform.authentication.RequireRole *) && execution(* *(..))")
    public void checkRoleAtClass(JoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        RequireRole requireRole = targetClass.getAnnotation(RequireRole.class);

        if (Objects.nonNull(requireRole)) {
            String requiredRole = requireRole.value();
            RequestContext ctx = RequestContextHolder.get();

            if (Objects.isNull(ctx) || !ctx.hasRole(requiredRole)) {
                throw new ForbiddenException(requiredRole);
            }
        }
    }
}
