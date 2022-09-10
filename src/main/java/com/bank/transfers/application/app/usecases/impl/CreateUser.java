package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.AlreadyUserException;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.app.usecases.ICreateUser;
import com.bank.transfers.application.domains.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUser implements ICreateUser {

    private final IUserRepository userRepository;

    public CreateUser(final IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(final User user) {
        validateUserAlreadyExists(user);
        final var userSave = userRepository.save(user);
        return userSave.cleanPassword();
    }

    private void validateUserAlreadyExists(final User user) {
        userRepository.findByDocumentOnlyNumbers(user.documentOnlyNumbers())
                .ifPresent((exists) -> {
                    throw new AlreadyUserException(String.format("Document %s already exist", exists.document()));
                });

        userRepository.findByEmail(user.email())
                .ifPresent((exists) -> {
                    throw new AlreadyUserException(String.format("Email %s already exist", exists.email()));
                });
    }
}
