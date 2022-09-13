package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashWithdrawal(
        String userId,
        BigDecimal value,
        WithdrawalType type,
        LocalDateTime create) {

    public static CashWithdrawal of(final User user, final BigDecimal value, final WithdrawalType type) {
        return new CashWithdrawal(user.id(), value, type, LocalDateTime.now());
    }
}
