package com.trading_platform.advice;

import com.trading_platform.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.resource.NoResourceFoundException;

@ControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public ProblemDetail exceptionHandler(BaseException e) {
        return getProblemDetail(e, e.getHttpStatus());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail noResourceFoundExceptionHandler(NoResourceFoundException e) {
        return getProblemDetail(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public void internalError(Exception e) {
        getProblemDetail(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail getProblemDetail(final Exception e, final HttpStatus httpStatus) {
        log.error(e.getMessage(), e);

        return ProblemDetail.forStatusAndDetail(httpStatus, e.getMessage());
    }
}
