package com.bank.transfers.application.infra.database.documents;

import com.bank.transfers.application.domains.CashDeposit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashDepositDocument(
        String userId,
        BigDecimal value,
        LocalDateTime create
) {
    public static CashDepositDocument from(final CashDeposit cashDeposit) {
        return new CashDepositDocument(cashDeposit.userId(), cashDeposit.value(), cashDeposit.create());
    }
}
