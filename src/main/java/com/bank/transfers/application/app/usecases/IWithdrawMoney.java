package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.CashWithdrawal;

import java.math.BigDecimal;

public interface IWithdrawMoney {
    CashWithdrawal execute(final BigDecimal value);

}
