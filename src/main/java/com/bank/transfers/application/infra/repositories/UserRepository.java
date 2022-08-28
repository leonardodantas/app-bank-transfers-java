package com.bank.transfers.application.infra.repositories;

import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.domains.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRepository implements IUserRepository {

    private final UserSpringData userSpringData;

    public UserRepository(final UserSpringData userSpringData) {
        this.userSpringData = userSpringData;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userSpringData.findByEmail(email);
    }

    @Override
    public Optional<User> findByDocument(String document) {
        return userSpringData.findByDocument(document);
    }

    @Override
    public Optional<User> findById(final String userId) {
        return userSpringData.findById(userId);
    }
}
