package com.beneboba.order_service.controller;


import com.beneboba.order_service.exception.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> constraintViolationException(ConstraintViolationException exception){

        log.error("constraintViolationException -> " + exception.getMessage());

        return Mono.just(new ErrorResponse(exception.getMessage()));
    }
}
