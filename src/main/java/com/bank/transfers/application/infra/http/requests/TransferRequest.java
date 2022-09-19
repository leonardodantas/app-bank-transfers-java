package com.bank.transfers.application.infra.http.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank
        String to,
        @Positive
        BigDecimal value
) {
}
