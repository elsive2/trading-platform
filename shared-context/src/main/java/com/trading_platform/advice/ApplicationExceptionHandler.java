package com.trading_platform.advice;

import com.trading_platform.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public ProblemDetail exceptionHandler(BaseException e) {
        log.error(e.getMessage(), e);

        return ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage());
    }
}
