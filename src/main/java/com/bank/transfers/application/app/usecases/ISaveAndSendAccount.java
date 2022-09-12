package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.Transfer;

public interface ISaveAndSendAccount {

    Transfer execute(final Transfer transfer, final Account account);
}
