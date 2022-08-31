package com.bank.transfers.application.app.repositories;

import com.bank.transfers.application.domains.CashDeposit;

import java.util.Optional;

public interface ICashDepositRepository {
    Optional<CashDeposit> save(final CashDeposit cashDeposit);
}
