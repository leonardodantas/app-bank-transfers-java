package com.bank.transfers.application.app.usecases;

public interface ILoginUser {

    String execute(final String email, final String password);
}
