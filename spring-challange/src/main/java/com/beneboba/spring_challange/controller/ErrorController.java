package com.beneboba.spring_challange.controller;

import com.beneboba.spring_challange.model.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<String>> constraintViolationException(ConstraintViolationException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.<String>builder()
                        .errors(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseResponse<String>> responseStatusException(ResponseStatusException exception){
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(BaseResponse.<String>builder()
                        .errors(exception.getReason())
                        .build());
    }
}
