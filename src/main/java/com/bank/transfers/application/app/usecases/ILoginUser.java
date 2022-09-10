package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.domains.Token;

public interface ILoginUser {

    Token execute(final String email, final String password);
}
