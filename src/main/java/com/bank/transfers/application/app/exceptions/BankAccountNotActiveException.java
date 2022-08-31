package com.bank.transfers.application.app.exceptions;

public class BankAccountNotActiveException extends RuntimeException {

    private final String message;
    public BankAccountNotActiveException(final String message) {
        this.message = message;
    }
}
