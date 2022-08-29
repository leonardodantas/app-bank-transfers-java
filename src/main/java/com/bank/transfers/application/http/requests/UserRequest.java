package com.bank.transfers.application.http.requests;

public record UserRequest (
        String nameComplete,
        String document,
        String email,
        String password
) {
}
