package com.bank.transfers.application.infra.database.documents;

import com.bank.transfers.application.domains.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Document("users")
public record UserDocument(
        @Id
        String id,
        String nameComplete,
        String document,
        String email,
        String password,
        @Indexed
        String documentOnlyNumbers
) {
    public static UserDocument from(final User user) {
        return new UserDocument(user.id(), user.nameComplete(), user.document(), user.email(), new BCryptPasswordEncoder().encode(user.password()), user.documentOnlyNumbers());
    }
}
