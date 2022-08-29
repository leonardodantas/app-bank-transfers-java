package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashDeposit (
        String id,
        String userId,
        BigDecimal value,
        LocalDateTime create
) {
}
