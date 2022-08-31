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
        Collection<CashDeposit> cashDeposits,

        Collection<CashWithdrawal> cashWithdrawal,
        LocalDateTime fistDeposit,
        LocalDateTime lastDeposit

) {

    public Account of(final CashDeposit cashDeposit) {
        final var cashDeposits = Stream.concat(this.cashDeposits.stream(), Stream.of(cashDeposit)).toList();
        final var fistDeposit = getFistDeposit(cashDeposits);
        return new Account(this.id, this.userId, this.account, this.number, this.openingDate, this.closingDate, this.active, cashDeposits, this.cashWithdrawal, fistDeposit, LocalDateTime.now());
    }

    private LocalDateTime getFistDeposit(final List<CashDeposit> cashDeposits) {
        final var fistDeposit = this.fistDeposit();
        if (cashDeposits.size() == 1) {
            return LocalDateTime.now();
        }
        return fistDeposit;
    }

    public BigDecimal amount() {
        final var cashWithdrawalAmount = cashWithdrawal.stream().map(CashWithdrawal::value).reduce(BigDecimal.ZERO, BigDecimal::add).negate();
        return cashDeposits.stream().map(CashDeposit::value).reduce(cashWithdrawalAmount, BigDecimal::add);
    }
}
