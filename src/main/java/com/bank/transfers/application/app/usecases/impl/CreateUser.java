package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.AlreadyUserException;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.app.usecases.ICreateUser;
import com.bank.transfers.application.domains.User;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class CreateUser implements ICreateUser {

    private final IUserRepository userRepository;

    public CreateUser(final IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(final User user) {
        validateUserAlreadyExists(user);
        final var userWithPasswordEncodeUpdate = getUserWithPasswordEncode(user);
        final var userSave = userRepository.save(userWithPasswordEncodeUpdate);
        return userSave.cleanPassword();
    }

    private User getUserWithPasswordEncode(final User user) {
        final var passwordEncode = Base64.getEncoder().encodeToString(user.password().getBytes());
        return user.updatePassword(passwordEncode);
    }

    private void validateUserAlreadyExists(final User user) {
        userRepository.findByDocument(user.documentOnlyNumbers())
                .ifPresent((exists) -> {
                    throw new AlreadyUserException(String.format("Document %s already exist", exists.document()));
                });

        userRepository.findByEmail(user.email())
                .ifPresent((exists) -> {
                    throw new AlreadyUserException(String.format("Email %s already exist", exists.email()));
                });
    }
}
