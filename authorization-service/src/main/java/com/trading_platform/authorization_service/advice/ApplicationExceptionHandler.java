package com.trading_platform.authorization_service.advice;

import com.trading_platform.authorization_service.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Optional;

@ControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public ProblemDetail exceptionHandler(BaseException e) {
        log.error(e.getMessage(), e);

        return ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemDetail validationExceptionHandler(WebExchangeBindException e) {
        Logger log = LoggerFactory.getLogger(getClass());
        log.error("Validation error occurred", e);

        Optional<String> firstErrorMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(msg -> msg != null && !msg.isEmpty())
                .findFirst();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(firstErrorMessage.orElse("Validation failed"));
        problemDetail.setTitle("Invalid Request");

        return problemDetail;
    }
}
