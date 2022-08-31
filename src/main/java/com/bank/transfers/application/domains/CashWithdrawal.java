package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CashWithdrawal(
        String id,
        String userId,
        BigDecimal value,
        LocalDateTime create) {

    public static CashWithdrawal of(final BigDecimal value, final User user) {
        return new CashWithdrawal(UUID.randomUUID().toString(), user.id(), value, LocalDateTime.now());
    }
}
