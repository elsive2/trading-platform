package com.trading_platform.customer_service.exception;

public class NotEnoughTicketAmountException extends RuntimeException {
    public NotEnoughTicketAmountException() {
        super("Not enough ticket amount");
    }
}
