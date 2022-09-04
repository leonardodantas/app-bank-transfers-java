package com.bank.transfers.application.app.repositories;

import com.bank.transfers.application.domains.Transfer;

public interface IBankTransferRepository {
    Transfer save(final Transfer transfer);
}
