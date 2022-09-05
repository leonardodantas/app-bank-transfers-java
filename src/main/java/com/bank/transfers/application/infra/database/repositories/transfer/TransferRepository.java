package com.bank.transfers.application.infra.database.repositories.transfer;

import com.bank.transfers.application.app.repositories.ITransferRepository;
import com.bank.transfers.application.domains.Transfer;
import com.bank.transfers.application.infra.database.converters.TransferDocumentConverter;
import com.bank.transfers.application.infra.database.documents.TransferDocument;
import org.springframework.stereotype.Component;

@Component
public class TransferRepository implements ITransferRepository {

    private final TransferSpringData transferSpringData;

    public TransferRepository(final TransferSpringData transferSpringData) {
        this.transferSpringData = transferSpringData;
    }

    @Override
    public Transfer save(final Transfer transfer) {
        final var transferDocument = TransferDocument.from(transfer);
        final var transferSave = transferSpringData.save(transferDocument);
        return TransferDocumentConverter.toDomain(transferSave);
    }
}
