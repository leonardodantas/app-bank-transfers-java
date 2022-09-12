package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.Transfer;

public interface ITransferMoney {

    void execute(final Transfer transfer);
}
