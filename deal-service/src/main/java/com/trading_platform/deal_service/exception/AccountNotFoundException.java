package com.trading_platform.deal_service.exception;

import com.trading_platform.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BaseException {
    private static final String message = "Account [id=%s] not found";

    public AccountNotFoundException(String id) {
        super(message.formatted(id), HttpStatus.NOT_FOUND);
    }
}
