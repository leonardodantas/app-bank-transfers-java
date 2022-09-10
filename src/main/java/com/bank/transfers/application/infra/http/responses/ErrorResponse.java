package com.bank.transfers.application.infra.http.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        String uuid,
        String field,
        String message,
        LocalDateTime dateTime

) {

    public static ErrorResponse from(final String message) {
        return new ErrorResponse(UUID.randomUUID().toString(), "", message, LocalDateTime.now());
    }

    public static ErrorResponse of(final String field, final String message) {
        return new ErrorResponse(UUID.randomUUID().toString(), field, message, LocalDateTime.now());
    }
}
