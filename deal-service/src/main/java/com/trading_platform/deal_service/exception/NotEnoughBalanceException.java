package com.trading_platform.deal_service.exception;

import com.trading_platform.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotEnoughBalanceException extends BaseException {
    public NotEnoughBalanceException() {
        super("Not enough balance", HttpStatus.BAD_REQUEST);
    }
}
