package com.bank.transfers.application.app.exceptions;

public class UserNotFoundException extends RuntimeException {

    private final String message;

    public UserNotFoundException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
