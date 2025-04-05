package com.trading_platform.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException{
    public ForbiddenException() {
        super("Forbidden", HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String roles) {
        super("Forbidden; Allowed roles - " + roles, HttpStatus.FORBIDDEN);
    }
}
