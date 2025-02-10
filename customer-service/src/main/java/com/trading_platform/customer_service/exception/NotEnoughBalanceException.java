package com.trading_platform.customer_service.exception;

import org.springframework.http.HttpStatus;

public class NotEnoughBalanceException extends BaseException {
    public NotEnoughBalanceException() {
        super("Not enough balance", HttpStatus.BAD_REQUEST);
    }
}
