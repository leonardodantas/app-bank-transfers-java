package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.usecases.IAddCashDeposit;
import com.bank.transfers.application.domains.CashDeposit;

import java.math.BigDecimal;

public class AddCashDeposit implements IAddCashDeposit {
    @Override
    public CashDeposit execute(BigDecimal value) {
        return null;
    }
}
