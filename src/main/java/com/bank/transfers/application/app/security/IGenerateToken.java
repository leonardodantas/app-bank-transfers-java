package com.bank.transfers.application.app.security;

import com.bank.transfers.application.domains.Token;

public interface IGenerateToken {

    Token execute(final String email, final String password);
}
