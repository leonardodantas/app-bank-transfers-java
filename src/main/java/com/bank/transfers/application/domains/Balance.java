package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Balance(
        String userId,
        BigDecimal value,
        LocalDateTime time
) {
    public static Balance of(final User user, final BigDecimal value) {
        return new Balance(user.id(), value, LocalDateTime.now());
    }
}
