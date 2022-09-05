package com.bank.transfers.application.domains;

import java.util.Collection;
import java.util.List;

public record BankTransactions(
        Collection<CashDeposit> cashDeposits,
        Collection<CashWithdrawal> cashWithdrawal
) {
    public static BankTransactions of(final CashDeposit cashDeposit) {
        return new BankTransactions(List.of(cashDeposit), List.of());
    }

    public static BankTransactions of(final CashDeposit cashDeposit, final CashWithdrawal cashWithdrawal) {
        return new BankTransactions(List.of(cashDeposit), List.of(cashWithdrawal));
    }

    public static BankTransactions of(final Collection<CashDeposit> cashDeposit, final Collection<CashWithdrawal> cashWithdrawal) {
        return new BankTransactions(cashDeposit, cashWithdrawal);
    }

    public static BankTransactions empty() {
        return new BankTransactions(List.of(), List.of());
    }

    public static BankTransactions of(final BankTransactions bankTransactions) {
        return new BankTransactions(bankTransactions.cashDeposits(), bankTransactions.cashWithdrawal());
    }

    public BankTransactions of(Collection<CashDeposit> cashDeposits) {
        return new BankTransactions(cashDeposits, this.cashWithdrawal);
    }
}
