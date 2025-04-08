package com.trading_platform.authorization_service.advice;

import com.trading_platform.advice.ApplicationExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Optional;

@Component
@Slf4j
@ControllerAdvice
public class AuthorizationExceptionHandler extends ApplicationExceptionHandler {
    private static final String DEFAULT_VALIDATION_MESSAGE = "Validation failed";
    private static final String DEFAULT_MESSAGE = "Invalid Request";

    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemDetail validationExceptionHandler(WebExchangeBindException e) {
        log.error(DEFAULT_VALIDATION_MESSAGE, e);

        Optional<String> firstErrorMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(msg -> msg != null && !msg.isEmpty())
                .findFirst();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(firstErrorMessage.orElse(DEFAULT_VALIDATION_MESSAGE));
        problemDetail.setTitle(DEFAULT_MESSAGE);

        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail badCredentialsExceptionHandler(BadCredentialsException e) {
        return getProblemDetail(e, HttpStatus.BAD_REQUEST);
    }
}
