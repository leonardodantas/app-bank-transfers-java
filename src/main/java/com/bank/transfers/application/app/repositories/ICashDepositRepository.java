package com.bank.transfers.application.app.repositories;

import com.bank.transfers.application.domains.CashDeposit;

public interface ICashDepositRepository {
    CashDeposit save(final CashDeposit cashDeposit);
}
