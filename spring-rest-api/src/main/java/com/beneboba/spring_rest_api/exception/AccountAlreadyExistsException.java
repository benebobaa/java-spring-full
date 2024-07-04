package com.beneboba.spring_rest_api.exception;

public class AccountAlreadyExistsException extends RuntimeException {

    private String message;

    public AccountAlreadyExistsException() {
        super();
    }

    public AccountAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public AccountAlreadyExistsException(final String message) {
        super(message);
        this.message = message;
    }

    public AccountAlreadyExistsException(final Throwable cause) {
        super(cause);
    }
}