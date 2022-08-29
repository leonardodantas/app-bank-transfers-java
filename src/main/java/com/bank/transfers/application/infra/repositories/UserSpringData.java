package com.bank.transfers.application.infra.repositories;

import com.bank.transfers.application.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserSpringData extends MongoRepository<User, String> {
    Optional<User> findByEmail(final String email);

    Optional<User> findByDocument(final String document);
}
