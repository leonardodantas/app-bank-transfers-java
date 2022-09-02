package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public record Account(
        String id,
        String userId,
        String account,
        String number,
        LocalDateTime openingDate,
        LocalDateTime closingDate,
        boolean active,
        BankTransactions bankTransactions,
        LocalDateTime fistDeposit,
        LocalDateTime lastDeposit

) {

    public Account of(final CashDeposit cashDeposit) {
        final var cashDeposits = Stream.concat(this.bankTransactions().cashDeposits().stream(), Stream.of(cashDeposit)).toList();
        final var fistDeposit = getFistDeposit(cashDeposits);
        return new Account(this.id, this.userId, this.account, this.number, this.openingDate, this.closingDate, this.active, bankTransactions.of(cashDeposits), fistDeposit, LocalDateTime.now());
    }

    private LocalDateTime getFistDeposit(final List<CashDeposit> cashDeposits) {
        final var fistDeposit = this.fistDeposit();
        if (cashDeposits.size() == 1) {
            return LocalDateTime.now();
        }
        return fistDeposit;
    }

    public BigDecimal amount() {
        final var cashWithdrawalAmount = this.bankTransactions().cashWithdrawal().stream().map(CashWithdrawal::value).reduce(BigDecimal.ZERO, BigDecimal::add).negate();
        return this.bankTransactions().cashDeposits().stream().map(CashDeposit::value).reduce(cashWithdrawalAmount, BigDecimal::add);
    }

    public Collection<CashDeposit> cashDeposits() {
        return this.bankTransactions().cashDeposits();
    }
}
