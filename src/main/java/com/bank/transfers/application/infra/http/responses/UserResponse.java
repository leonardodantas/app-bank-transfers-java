package com.bank.transfers.application.infra.http.responses;

import com.bank.transfers.application.domains.User;

public record UserResponse(
        String id,
        String nameComplete,
        String document,
        String email
) {
    public static UserResponse from(final User user) {
        return new UserResponse(user.id(), user.nameComplete(), user.document(), user.email());
    }
}
