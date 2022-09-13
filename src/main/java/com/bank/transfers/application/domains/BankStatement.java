package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankStatement(
        BigDecimal value,
        LocalDateTime time,
        BankStatementType type
) {
    public static BankStatement of(final BigDecimal value, final BankStatementType type) {
        return new BankStatement(value, LocalDateTime.now(), type);
    }
}
