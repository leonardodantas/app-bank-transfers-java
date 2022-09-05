package com.bank.transfers.application.infra.database.converters;

import com.bank.transfers.application.domains.User;
import com.bank.transfers.application.infra.database.documents.UserDocument;

public class UserDocumentConverter {

    public static User toDomain(final UserDocument document) {
        return new User(document.id(), document.nameComplete(), document.document(), document.email(), "");
    }
}
