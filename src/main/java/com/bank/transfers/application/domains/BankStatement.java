package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankStatement(
        BigDecimal value,
        BankStatementType type,
        LocalDateTime time
) implements Comparable<BankStatement> {
    public static BankStatement of(final BigDecimal value, final BankStatementType type, final LocalDateTime time) {
        return new BankStatement(value, type, time);
    }

    @Override
    public int compareTo(final BankStatement bankStatement) {
        if (bankStatement.time().isBefore(this.time)) {
            return -1;
        }
        return 1;
    }
}
