package com.trading_platform.authorization_service.exception;

import com.trading_platform.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException() {
        super("User already exists", HttpStatus.BAD_REQUEST);
    }
}
