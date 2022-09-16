package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.Account;

public interface IGetAccount {

    Account execute();
    Account execute(final String accountNumber);
}
