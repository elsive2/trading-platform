package com.trading_platform.deal_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException {
    protected String message;
    protected HttpStatus httpStatus;
}
