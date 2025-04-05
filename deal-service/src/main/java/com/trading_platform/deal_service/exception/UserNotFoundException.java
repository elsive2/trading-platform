package com.trading_platform.deal_service.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    private static final String message = "User [id=%d] not found";

    public UserNotFoundException(Integer id) {
        super(message.formatted(id), HttpStatus.NOT_FOUND);
    }
}
