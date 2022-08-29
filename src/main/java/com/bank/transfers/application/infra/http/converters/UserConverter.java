package com.bank.transfers.application.infra.http.converters;

import com.bank.transfers.application.domains.User;
import com.bank.transfers.application.infra.http.requests.UserRequest;


import java.util.UUID;

public class UserConverter {
    public static User toDomain(final UserRequest json) {
        return new User(UUID.randomUUID().toString(), json.nameComplete(), json.document(), json.email(), json.password());
}
