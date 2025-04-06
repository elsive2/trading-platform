package com.trading_platform.deal_service.exception;

import com.trading_platform.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotEnoughTicketAmountException extends BaseException {
    public NotEnoughTicketAmountException() {
        super("Not enough ticket amount", HttpStatus.BAD_REQUEST);
    }
}
