package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashDeposit(
        String userId,
        BigDecimal value,
        TransferType transferType,
        LocalDateTime create
) {
    public static CashDeposit of(final User user, final BigDecimal value, TransferType transferType) {
        return new CashDeposit(user.id(), value, transferType, LocalDateTime.now());
    }
}
