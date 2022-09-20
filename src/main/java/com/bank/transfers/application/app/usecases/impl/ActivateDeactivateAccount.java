package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.AccountAlreadyEnableDisableException;
import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
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
                .orElseThrow(() -> new BankAccountNotFoundException(String.format("User %s don't have an account", user.id())));

        if (account.active()) {
            throw new AccountAlreadyEnableDisableException(String.format("Account %s is already enable", account.account()));
        }

        accountRepository.save(account.enable());
    }

    @Override
    public void deactivate() {
        final var account = getAccount.execute();
        accountRepository.save(account.disable());
    }
}
