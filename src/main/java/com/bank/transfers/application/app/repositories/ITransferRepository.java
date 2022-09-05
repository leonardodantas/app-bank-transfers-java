package com.bank.transfers.application.app.repositories;

import com.bank.transfers.application.domains.Transfer;

public interface ITransferRepository {
    Transfer save(final Transfer transfer);
}
