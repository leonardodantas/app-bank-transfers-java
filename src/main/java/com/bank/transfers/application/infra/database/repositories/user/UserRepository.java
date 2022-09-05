package com.bank.transfers.application.infra.database.repositories.user;

import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.domains.User;
import com.bank.transfers.application.infra.database.converters.UserDocumentConverter;
import com.bank.transfers.application.infra.database.documents.UserDocument;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRepository implements IUserRepository {

    private final UserSpringData userSpringData;

    public UserRepository(final UserSpringData userSpringData) {
        this.userSpringData = userSpringData;
    }

    @Override
    public User save(final User user) {
        final var userDocument = UserDocument.from(user);
        try {
            final var userSave = this.userSpringData.save(userDocument);
            return UserDocumentConverter.toDomain(userSave);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userSpringData.findByEmail(email).map(UserDocumentConverter::toDomain);
    }

    @Override
    public Optional<User> findByDocument(String document) {
        return userSpringData.findByDocument(document).map(UserDocumentConverter::toDomain);
    }

    @Override
    public Optional<User> findById(final String userId) {
        return userSpringData.findById(userId).map(UserDocumentConverter::toDomain);
    }
}
