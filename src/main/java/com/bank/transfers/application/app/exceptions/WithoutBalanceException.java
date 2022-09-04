package com.bank.transfers.application.app.exceptions;

public class WithoutBalanceException extends RuntimeException {

    private final String message;

    public WithoutBalanceException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
