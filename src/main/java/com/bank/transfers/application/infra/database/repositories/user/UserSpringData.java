package com.bank.transfers.application.infra.database.repositories.user;

import com.bank.transfers.application.infra.database.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserSpringData extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByEmail(final String email);

    Optional<UserDocument> findByDocument(final String document);
}
