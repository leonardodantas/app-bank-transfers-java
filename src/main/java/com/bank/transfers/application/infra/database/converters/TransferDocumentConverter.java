package com.bank.transfers.application.infra.database.converters;

import com.bank.transfers.application.domains.Transfer;
import com.bank.transfers.application.infra.database.documents.TransferDocument;

public class TransferDocumentConverter {

    public static Transfer toDomain(final TransferDocument document) {
        return new Transfer(document.id(), document.to(), document.from(), document.value(), document.date());
    }
}
