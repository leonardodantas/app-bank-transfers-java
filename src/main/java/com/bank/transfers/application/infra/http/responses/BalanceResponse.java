package com.bank.transfers.application.infra.http.responses;

import com.bank.transfers.application.domains.Balance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BalanceResponse(
        String userId,
        BigDecimal value,
        LocalDateTime time
) {
    public static BalanceResponse from(final Balance balance) {
        return new BalanceResponse(balance.userId(), balance.value(), balance.time());
    }
}
