package com.bank.transfers.application.infra.database.documents;

import com.bank.transfers.application.domains.Account;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record AccountDocument(
        @Id
        String id,
        String userId,
        String account,
        String number,
        LocalDateTime openingDate,
        LocalDateTime closingDate,
        boolean active,
        BankTransactionsDocument bankTransactions,
        LocalDateTime fistDeposit,
        LocalDateTime lastDeposit
) {
        public static AccountDocument from(final Account account) {
                return new AccountDocument(account.id(), account.userId(), account.account(), account.number(), account.openingDate(), account.closingDate(), account.active(), BankTransactionsDocument.from(account.bankTransactions()), account.fistDeposit(), account.lastDeposit());
        }
}
