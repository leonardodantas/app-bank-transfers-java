package com.bank.transfers.application.infra.http.converters;

import com.bank.transfers.application.domains.User;
import com.bank.transfers.application.infra.http.requests.UserRequest;

import java.util.UUID;

public class UserConverter {
    public static User toDomain(final UserRequest json) {
        final var documentOnlyNumbers = json.document().replaceAll("\\D", "");
        return new User(UUID.randomUUID().toString(), json.nameComplete(), json.document(), documentOnlyNumbers, json.email(), json.password());
    }
}
