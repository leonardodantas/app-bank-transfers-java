package com.bank.transfers.application.app.exceptions;

public class LogisticsTransferException extends RuntimeException {

    private final String message;

    public LogisticsTransferException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
