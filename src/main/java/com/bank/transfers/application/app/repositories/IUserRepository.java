package com.bank.transfers.application.app.repositories;

import com.bank.transfers.application.domains.User;

import java.util.Optional;

public interface IUserRepository {
    User save(final User user);

    Optional<User> findByEmail(final String email);

    Optional<User> findByDocumentOnlyNumbers(final String document);

    Optional<User> findById(final String userId);

}
