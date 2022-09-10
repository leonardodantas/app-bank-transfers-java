package com.bank.transfers.application.infra.http.requests;

import com.bank.transfers.application.infra.http.annotations.DocumentValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public record UserRequest(
        @Size(min = 10, max = 120)
        String nameComplete,
        @DocumentValid
        String document,
        @Email
        String email,
        @Size(min = 6, max = 8)
        String password
) {
}
