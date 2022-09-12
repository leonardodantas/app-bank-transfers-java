package com.bank.transfers.application.infra.database.repositories.account;

import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.infra.database.documents.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountSpringData extends MongoRepository<AccountDocument, String> {
    Optional<AccountDocument> findByUserId(final String userId);
    Optional<Account> findByAccount(final String account);
}
