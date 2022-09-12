package com.bank.transfers.application.infra.database.converters;

import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.domains.TransferType;
import com.bank.transfers.application.infra.database.documents.CashDepositDocument;

public class CashDepositDocumentConverter {
    public static CashDeposit toDomain(final CashDepositDocument document) {
        return new CashDeposit(document.userId(), document.value(), TransferType.valueOf(document.transferType().name()), document.create());
    }
}
