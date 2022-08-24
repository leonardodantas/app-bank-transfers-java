package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.User;

public interface ICreateUser {
    User execute(final User user);
}
