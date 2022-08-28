package com.bank.transfers.application.app.security;

public interface IGenerateToken {

    String execute(final String email, final String password);
}
