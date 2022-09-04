package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transfer(
        String id,
        String to,
        String from,
        BigDecimal value,
        LocalDateTime date
) {
    public static Transfer of(final String from, final BigDecimal value) {
        return new Transfer(UUID.randomUUID().toString(), "", from, value, LocalDateTime.now());
    }

    public Transfer withAccount(final Account account) {
        return new Transfer(this.id, account.account(), this.from, this.value, this.date);
    }
}
