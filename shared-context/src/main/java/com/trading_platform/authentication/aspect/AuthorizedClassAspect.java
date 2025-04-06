package com.trading_platform.authentication.aspect;

import com.trading_platform.authentication.Authorized;
import com.trading_platform.authentication.RequestContext;
import com.trading_platform.authentication.RequestContextHolder;
import com.trading_platform.exception.UnauthorizedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Objects;

@Aspect
public class AuthorizedClassAspect {
    @Before("within(@com.trading_platform.authentication.Authorized *) && execution(* *(..))")
    public void checkRoleAtClass(JoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Authorized authorized = targetClass.getAnnotation(Authorized.class);

        if (Objects.nonNull(authorized)) {
            RequestContext ctx = RequestContextHolder.get();

            if (Objects.isNull(ctx) || Objects.isNull(ctx.getUserId())) {
                throw new UnauthorizedException();
            }
        }
    }
}
