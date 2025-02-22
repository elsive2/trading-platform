package com.trading_platform.stock_service.exception;

import org.springframework.http.HttpStatus;

public class StockNotFoundException extends BaseException {
    private static final String message = "Stock [id=%d] not found";

    public StockNotFoundException(Integer id) {
        super(message.formatted(id), HttpStatus.NOT_FOUND);
    }
}
