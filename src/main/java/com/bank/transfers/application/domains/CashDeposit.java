package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CashDeposit(
        String id,
        String userId,
        BigDecimal value,
        LocalDateTime create
) {
    public static CashDeposit of(final BigDecimal value, final User user) {
        return new CashDeposit(UUID.randomUUID().toString(), user.id(), value, LocalDateTime.now());
    }
}
