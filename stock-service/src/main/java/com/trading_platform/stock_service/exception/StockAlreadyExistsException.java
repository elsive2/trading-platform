package com.trading_platform.stock_service.exception;

import org.springframework.http.HttpStatus;

public class StockAlreadyExistsException extends BaseException {
    private static final String message = "Stock [name=%s] already exists";

    public StockAlreadyExistsException(String name) {
        super(String.format(message, name), HttpStatus.BAD_REQUEST);
    }
}
