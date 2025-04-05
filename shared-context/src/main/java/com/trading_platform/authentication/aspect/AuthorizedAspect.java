package com.trading_platform.authentication.aspect;


import com.trading_platform.authentication.Authorized;
import com.trading_platform.authentication.RequestContext;
import com.trading_platform.authentication.RequestContextHolder;
import com.trading_platform.exception.UnauthorizedException;
import jakarta.annotation.Priority;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Objects;

@Aspect
@Priority(1)
public class AuthorizedAspect {
    @Before("@annotation(authorized)")
    public void checkRole(Authorized authorized) {
        RequestContext ctx = RequestContextHolder.get();
        if (Objects.isNull(ctx)) {
            throw new UnauthorizedException();
        }
    }
}
