package com.bank.transfers.application.infra.http.responses;

import com.bank.transfers.application.domains.Token;

import java.time.LocalDateTime;

public record TokenResponse(
        String token,
        LocalDateTime createAt,
        LocalDateTime expiredAt
) {
    public static TokenResponse from(final Token token) {
        return new TokenResponse(token.token(), token.createAt(), token.expiredAt());
    }
}
