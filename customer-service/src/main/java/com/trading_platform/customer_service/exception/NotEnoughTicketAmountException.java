package com.trading_platform.customer_service.exception;

import org.springframework.http.HttpStatus;

public class NotEnoughTicketAmountException extends BaseException {
    public NotEnoughTicketAmountException() {
        super("Not enough ticket amount", HttpStatus.BAD_REQUEST);
    }
}
