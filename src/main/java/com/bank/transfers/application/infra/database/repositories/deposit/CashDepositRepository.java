package com.bank.transfers.application.infra.database.repositories.deposit;

import com.bank.transfers.application.app.repositories.ICashDepositRepository;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.infra.database.converters.CashDepositDocumentConverter;
import com.bank.transfers.application.infra.database.documents.CashDepositDocument;
import org.springframework.stereotype.Component;

@Component
public class CashDepositRepository implements ICashDepositRepository {

    private final CashDepositSpringData cashDepositSpringData;

    public CashDepositRepository(final CashDepositSpringData cashDepositSpringData) {
        this.cashDepositSpringData = cashDepositSpringData;
    }

    @Override
    public CashDeposit save(final CashDeposit cashDeposit) {
        try {
            final var cashDepositDocument = CashDepositDocument.from(cashDeposit);
            final var cashDepositSave = cashDepositSpringData.save(cashDepositDocument);
            return CashDepositDocumentConverter.toDomain(cashDepositSave);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
