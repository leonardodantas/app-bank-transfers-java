package com.bank.transfers.application.domains;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
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

    public static Account of(final String id, final String userId, final String account, final String number, final LocalDateTime openingDate, final LocalDateTime closingDate, final LocalDateTime fistDeposit, final LocalDateTime lastDeposit) {
        return new Account(id, userId, account, number, openingDate, closingDate, true, BankTransactions.empty(), fistDeposit, lastDeposit);
    }

    public static Account of(final User user) {
        final var accountNumberBase = String.valueOf(Instant.now().getNano());
        final var accountBase = accountNumberBase.substring(0, 6) + user.documentOnlyNumbers().substring(0, 2);
        final var number = user.documentOnlyNumbers().substring(user.documentOnlyNumbers().length() - 1);
        return new Account(UUID.randomUUID().toString(), user.id(), accountBase,
                number, LocalDateTime.now(), LocalDateTime.now().minusDays(1), true, BankTransactions.empty(), LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1));
    }

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

    public Account withDeposit(final CashDeposit cashDeposit) {
        return new Account(this.id, this.userId, this.account, this.number, this.openingDate, this.closingDate, this.active, BankTransactions.of(cashDeposit), fistDeposit, LocalDateTime.now());
    }

    public Account disable() {
        return new Account(this.id, this.userId, this.account, this.number, this.openingDate, this.closingDate, false, this.bankTransactions, fistDeposit, LocalDateTime.now());
    }

    public Account withBankTransactions(final BankTransactions bankTransactions) {
        return new Account(this.id, this.userId, this.account, this.number, this.openingDate, this.closingDate, this.active, bankTransactions, fistDeposit, LocalDateTime.now());
    }

    public Account withWithdrawal(final CashWithdrawal cashWithdrawal) {
        return new Account(this.id, this.userId, this.account, this.number, this.openingDate, this.closingDate, this.active, this.bankTransactions.withWithdrawal(cashWithdrawal), fistDeposit, LocalDateTime.now());
    }
}
