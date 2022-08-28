package com.bank.transfers.application.infra.http.requests;

public record UserLoginRequest(
        String email,
        String password
) {
}
