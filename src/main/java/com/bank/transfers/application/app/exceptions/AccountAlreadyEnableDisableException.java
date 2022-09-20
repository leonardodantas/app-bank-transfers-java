package com.bank.transfers.application.app.exceptions;

public class AccountAlreadyEnableDisableException extends RuntimeException {

    private final String message;

    public AccountAlreadyEnableDisableException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
