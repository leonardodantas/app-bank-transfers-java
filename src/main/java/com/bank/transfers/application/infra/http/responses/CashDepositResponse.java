package com.bank.transfers.application.infra.http.responses;

import com.bank.transfers.application.domains.CashDeposit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashDepositResponse(
        String userId,
        BigDecimal value,
        LocalDateTime create
) {
    public static CashDepositResponse from(final CashDeposit cashDeposit) {
        return new CashDepositResponse(cashDeposit.userId(), cashDeposit.value(), cashDeposit.create());
    }
}
