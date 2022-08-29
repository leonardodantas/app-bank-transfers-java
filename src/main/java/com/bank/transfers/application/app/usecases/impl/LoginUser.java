package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.security.IGenerateToken;
import com.bank.transfers.application.app.usecases.ILoginUser;
import org.springframework.stereotype.Service;

@Service
public class LoginUser implements ILoginUser {

    private final IGenerateToken generateToken;

    public LoginUser(final IGenerateToken generateToken) {
        this.generateToken = generateToken;
    }

    @Override
    public String execute(final String email, final String password) {
        return this.generateToken.execute(email, password);
    }

}
