package com.bank.transfers.application.app.exceptions;

public class AlreadyUserException extends RuntimeException {

    private final String message;

    public AlreadyUserException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
