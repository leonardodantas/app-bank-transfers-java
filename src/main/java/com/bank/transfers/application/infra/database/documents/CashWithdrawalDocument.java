package com.bank.transfers.application.infra.database.documents;

import com.bank.transfers.application.domains.CashWithdrawal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashWithdrawalDocument(
        String userId,
        BigDecimal value,
        WithdrawalTypeDocument type,
        LocalDateTime create
) {
    public static CashWithdrawalDocument from(final CashWithdrawal cashWithdrawal) {
        return new CashWithdrawalDocument(cashWithdrawal.userId(), cashWithdrawal.value(), WithdrawalTypeDocument.valueOf(cashWithdrawal.type().name()), cashWithdrawal.create());
    }
}
