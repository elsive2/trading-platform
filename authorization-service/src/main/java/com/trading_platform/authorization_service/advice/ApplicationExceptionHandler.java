package com.trading_platform.authorization_service.advice;

import com.trading_platform.authorization_service.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {
    private static final String DEFAULT_VALIDATION_MESSAGE = "Validation failed";
    private static final String DEFAULT_MESSAGE = "Invalid Request";

    @ExceptionHandler(BaseException.class)
    public ProblemDetail exceptionHandler(BaseException e) {
        log.error(e.getMessage(), e);

        return ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage());
    }

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
}
