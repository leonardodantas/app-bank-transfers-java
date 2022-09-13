package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IGetBalance;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.Balance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GetBalance implements IGetBalance {

    private final IGetUserToken getUserToken;
    private final IAccountRepository accountRepository;

    public GetBalance(final IGetUserToken getUserToken, final IAccountRepository accountRepository) {
        this.getUserToken = getUserToken;
        this.accountRepository = accountRepository;
    }

    @Override
    public Balance execute() {
        final var user = getUserToken.execute();
        final var value = accountRepository.findByUserId(user.id()).map(Account::amount).orElse(BigDecimal.ZERO);
        return Balance.of(user, value);
    }
}
