package com.bank.transfers.application.domains;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public record Token(
        String token,
        LocalDateTime createAt,
        LocalDateTime expiredAt
) {
    public static Token of(final String token, final Date createAt, final Date expirationAt) {
        return new Token(token, getLocalDateTime(createAt), getLocalDateTime(expirationAt));
    }

    private static LocalDateTime getLocalDateTime(final Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
