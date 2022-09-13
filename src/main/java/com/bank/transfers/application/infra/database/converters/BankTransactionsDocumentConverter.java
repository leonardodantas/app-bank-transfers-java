package com.bank.transfers.application.infra.database.converters;

import com.bank.transfers.application.domains.*;
import com.bank.transfers.application.infra.database.documents.BankTransactionsDocument;
import com.bank.transfers.application.infra.database.documents.CashDepositDocument;
import com.bank.transfers.application.infra.database.documents.CashWithdrawalDocument;

public class BankTransactionsDocumentConverter {

    public static BankTransactions toDomain(final BankTransactionsDocument document) {
        final var cashDeposit = document.cashDeposits().stream().map(BankTransactionsDocumentConverter::toDomain).toList();
        final var cashWithdrawal = document.cashWithdrawal().stream().map(BankTransactionsDocumentConverter::toDomain).toList();
        return new BankTransactions(cashDeposit, cashWithdrawal);
    }

    private static CashDeposit toDomain(final CashDepositDocument document) {
        return new CashDeposit(document.userId(), document.value(), TransferType.valueOf(document.transferType().name()), document.create());
    }

    private static CashWithdrawal toDomain(final CashWithdrawalDocument document) {
        return new CashWithdrawal(document.userId(), document.value(), WithdrawalType.valueOf(document.type().name()), document.create());
    }
}
