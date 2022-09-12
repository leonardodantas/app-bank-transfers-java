package com.bank.transfers.application.infra.database.documents;

import com.bank.transfers.application.domains.CashDeposit;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("cashDeposits")
public record CashDepositDocument(
        String userId,
        BigDecimal value,
        LocalDateTime create
) {
    public static CashDepositDocument from(final CashDeposit cashDeposit) {
        return new CashDepositDocument(cashDeposit.userId(), cashDeposit.value(), cashDeposit.create());
    }
}
