package com.bank.transfers.application.infra.database.documents;

import com.bank.transfers.application.domains.BankTransactions;

import java.util.Collection;

public record BankTransactionsDocument(
        Collection<CashDepositDocument> cashDeposits,
        Collection<CashWithdrawalDocument> cashWithdrawal
) {
    public static BankTransactionsDocument from(final BankTransactions bankTransactions) {
        final var cashDeposits = bankTransactions.cashDeposits().stream().map(CashDepositDocument::from).toList();
        final var cashWithdrawal = bankTransactions.cashWithdrawal().stream().map(CashWithdrawalDocument::from).toList();
        return new BankTransactionsDocument(cashDeposits, cashWithdrawal);
    }
}
