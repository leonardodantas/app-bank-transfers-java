package com.bank.transfers.application.infra.database.converters;

import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.infra.database.documents.AccountDocument;

public class AccountDocumentConverter {

    public static Account toDomain(final AccountDocument document) {
        return new Account(document.id(), document.userId(), document.account(), document.number(), document.openingDate(), document.closingDate(), document.active(), BankTransactionsDocumentConverter.toDomain(document.bankTransactions()), document.fistDeposit(), document.lastDeposit());
    }
}
