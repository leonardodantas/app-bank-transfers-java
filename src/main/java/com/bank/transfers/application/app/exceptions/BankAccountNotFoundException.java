package com.bank.transfers.application.app.exceptions;

public class BankAccountNotFoundException extends RuntimeException {

    private final String message;
    public BankAccountNotFoundException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
