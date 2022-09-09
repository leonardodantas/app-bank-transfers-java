package com.bank.transfers.application.infra.http.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserRequest (
        @NotBlank
        String nameComplete,
        String document,
        @Email
        String email,
        @Size
        String password
) {
}
