package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.AlreadyUserException;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.app.usecases.ICreateUser;
import com.bank.transfers.application.domains.User;
import org.springframework.stereotype.Service;

@Service
public class CreateUser implements ICreateUser {

    private final IUserRepository userRepository;

    public CreateUser(final IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(final User user) {
        userRepository.findByDocument(user.getDocumentOnlyNumbers())
                .ifPresent((exists) -> {
                    throw new AlreadyUserException(String.format("Document %s already exist", exists.getDocument()));
                });

        userRepository.findByEmail(user.getEmail())
                .ifPresent((exists) -> {
                    throw new AlreadyUserException(String.format("Email %s already exist", exists.getEmail()));
                });

        return userRepository.save(user);
    }
}
