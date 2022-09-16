package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IGetAccount;
import com.bank.transfers.application.app.usecases.IGetBalance;
import com.bank.transfers.application.domains.Balance;
import org.springframework.stereotype.Service;

@Service
public class GetBalance implements IGetBalance {

    private final IGetUserToken getUserToken;
    private final IGetAccount getAccount;

    public GetBalance(final IGetUserToken getUserToken, final IGetAccount getAccount) {
        this.getUserToken = getUserToken;
        this.getAccount = getAccount;
    }

    @Override
    public Balance execute() {
        final var user = getUserToken.execute();
        final var account = getAccount.execute();
        return Balance.of(user, account.amount());
    }
}
