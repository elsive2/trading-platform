package com.trading_platform.customer_service.exception;

public class CustomerNotFoundException extends RuntimeException {
    private static final String message = "Customer [id=%d] not found";

    public CustomerNotFoundException(Integer id) {
        super(message.formatted(id));
    }
}
