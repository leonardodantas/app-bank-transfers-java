package com.bank.transfers.application.infra.http.requests;

public record UserRequest (
        String nameComplete,
        String document,
        String email,
        String password
) {
}
