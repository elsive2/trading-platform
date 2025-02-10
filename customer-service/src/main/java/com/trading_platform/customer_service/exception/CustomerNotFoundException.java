package com.trading_platform.customer_service.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends BaseException {
    private static final String message = "Customer [id=%d] not found";

    public CustomerNotFoundException(Integer id) {
        super(message.formatted(id), HttpStatus.NOT_FOUND);
    }
}
