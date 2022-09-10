package com.bank.transfers.application.infra.http.requests;

import javax.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
