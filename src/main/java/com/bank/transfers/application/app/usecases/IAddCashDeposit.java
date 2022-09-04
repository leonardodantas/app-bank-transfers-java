package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.CashDeposit;

import java.math.BigDecimal;

public interface IAddCashDeposit {

    CashDeposit execute(final BigDecimal value);
}
