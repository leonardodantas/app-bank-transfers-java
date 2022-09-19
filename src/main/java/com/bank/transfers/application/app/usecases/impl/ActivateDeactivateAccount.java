package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IActivateDeactivateAccount;
import com.bank.transfers.application.app.usecases.IGetAccount;
import org.springframework.stereotype.Service;

@Service
public class ActivateDeactivateAccount implements IActivateDeactivateAccount {

    private final IGetAccount getAccount;
    private final IAccountRepository accountRepository;
    private final IGetUserToken getUserToken;

    public ActivateDeactivateAccount(final IGetAccount getAccount, final IAccountRepository accountRepository, final IGetUserToken getUserToken) {
        this.getAccount = getAccount;
        this.accountRepository = accountRepository;
        this.getUserToken = getUserToken;
    }

    @Override
    public void activate() {
        final var user = getUserToken.execute();
        final var account = accountRepository.findByUserId(user.id())
                .orElseThrow();

        if (account.active()) {
            throw new RuntimeException();
        }

        accountRepository.save(account.enable());
    }

    @Override
    public void deactivate() {
        final var account = getAccount.execute();
        accountRepository.save(account.disable());
    }
}
