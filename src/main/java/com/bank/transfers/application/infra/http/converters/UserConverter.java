package com.bank.transfers.application.infra.http.converters;

import com.bank.transfers.application.domains.User;
import com.bank.transfers.application.infra.http.requests.UserRequest;

public class UserConverter {
    public static User toDomain(final UserRequest json) {
        return new User(json.nameComplete(), json.document(), json.email(), json.password());
    }
}
