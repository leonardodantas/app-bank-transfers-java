package com.bank.transfers.application.infra.database.repositories.deposit;

import com.bank.transfers.application.infra.database.documents.CashDepositDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CashDepositSpringData extends MongoRepository<CashDepositDocument, String> {
}
