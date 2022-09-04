package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.Transfer;

public interface IBankTransfer {

    Transfer execute(final Transfer transfer);
}
