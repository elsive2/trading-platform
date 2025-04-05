package com.trading_platform.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends BaseException{
    public ServiceException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
