package com.trading_platform.customer_service.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException() {
        super("Not enough balance");
    }
}
