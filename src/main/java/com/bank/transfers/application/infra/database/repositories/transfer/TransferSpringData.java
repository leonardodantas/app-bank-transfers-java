package com.bank.transfers.application.infra.database.repositories.transfer;

import com.bank.transfers.application.infra.database.documents.TransferDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransferSpringData extends MongoRepository<TransferDocument, String> {
}
