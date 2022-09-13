package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.BankStatement;

import java.util.Collection;

public interface IGetBankStatement {

    Collection<BankStatement> execute();
}
