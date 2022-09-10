package com.bank.transfers.application.infra.http.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public record UserRequest(
        @Size(min = 10, max = 120)
        String nameComplete,
        String document,
        @Email
        String email,
        @Size(min = 6, max = 8)
        String password
) {
}
