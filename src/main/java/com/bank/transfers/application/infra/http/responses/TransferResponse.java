package com.bank.transfers.application.infra.http.responses;

import com.bank.transfers.application.domains.Transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        String to,
        String from,
        BigDecimal value,
        LocalDateTime date
) {
    public static TransferResponse from(final Transfer transfer) {
        return new TransferResponse(transfer.to(), transfer.from(), transfer.value(), transfer.date());
    }
}
