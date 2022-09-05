package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CashDeposit(
        String userId,
        BigDecimal value,
        LocalDateTime create
) {
    public static CashDeposit of(final User user, final BigDecimal value) {
        return new CashDeposit(user.id(), value, LocalDateTime.now());
    }
}
