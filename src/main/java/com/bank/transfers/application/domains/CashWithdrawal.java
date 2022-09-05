package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashWithdrawal(
        String userId,
        BigDecimal value,
        LocalDateTime create) {

    public static CashWithdrawal of(final User user, final BigDecimal value) {
        return new CashWithdrawal(user.id(), value, LocalDateTime.now());
    }
}
