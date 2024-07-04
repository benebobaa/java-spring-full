package com.beneboba.spring_rest_api.controller;

import com.beneboba.spring_rest_api.exception.AccountAlreadyExistsException;
import com.beneboba.spring_rest_api.exception.ErrorResponse;
import com.beneboba.spring_rest_api.exception.NoSuchAccountExistsException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({NoSuchAccountExistsException.class, NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchAccountExistsException(final Exception ex, final WebRequest request) {
        logger.error("handle NoSuchAccountExistsException, NoSuchElementException", ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return createResponseEntity(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }

    @ExceptionHandler({AccountAlreadyExistsException.class})
    public ResponseEntity<Object> handleCustomerAlreadyExistsException(final Exception ex, final WebRequest request) {
        logger.error("handle AccountAlreadyExistsException", ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        logger.error("handle Exception", ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        logger.error("handle     MethodArgumentNotValidException", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors.toString());
        return createResponseEntity(errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }
}