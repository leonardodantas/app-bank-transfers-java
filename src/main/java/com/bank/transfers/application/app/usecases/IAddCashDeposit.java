package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.CashDeposit;

import java.math.BigDecimal;
import java.util.Optional;

public interface IAddCashDeposit {

    Optional<CashDeposit> execute(final BigDecimal value);
}
