package com.bank.transfers.application.infra.http.controllers.advice;

import com.bank.transfers.application.app.exceptions.AlreadyUserException;

import java.time.LocalDateTime;
import java.util.UUID;

public record ErrorResponse(
        String uuid,
        String message,
        LocalDateTime dateTime

) {

    public static ErrorResponse from(final String message) {
        return new ErrorResponse(UUID.randomUUID().toString(), message, LocalDateTime.now());
    }
}
