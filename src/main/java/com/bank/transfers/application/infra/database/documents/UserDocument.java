package com.bank.transfers.application.infra.database.documents;

import com.bank.transfers.application.domains.User;
import org.springframework.data.annotation.Id;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record UserDocument(
        @Id
        String id,
        String nameComplete,
        String document,
        String email,
        String password,
        String documentOnlyNumbers
) {
    public static UserDocument from(final User user) {
        return new UserDocument(user.id(), user.nameComplete(), user.document(), user.email(), new BCryptPasswordEncoder().encode(user.password()), user.documentOnlyNumbers());
    }
}
