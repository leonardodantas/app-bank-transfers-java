package com.bank.transfers.application.infra.http.responses;

import com.bank.transfers.application.domains.CashWithdrawal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashWithdrawalResponse(
        String userId,
        BigDecimal value,
        LocalDateTime create
) {
    public static CashWithdrawalResponse from(final CashWithdrawal cashWithdrawal) {
        return new CashWithdrawalResponse(cashWithdrawal.userId(), cashWithdrawal.value(), cashWithdrawal.create());
    }
}
