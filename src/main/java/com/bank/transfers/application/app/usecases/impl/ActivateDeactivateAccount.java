package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.AccountAlreadyEnableDisableException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.usecases.IActivateDeactivateAccount;
import com.bank.transfers.application.app.usecases.IGetAccount;
import org.springframework.stereotype.Service;

@Service
public class ActivateDeactivateAccount implements IActivateDeactivateAccount {

    private final IGetAccount getAccount;
    private final IAccountRepository accountRepository;

    public ActivateDeactivateAccount(final IGetAccount getAccount, final IAccountRepository accountRepository) {
        this.getAccount = getAccount;
        this.accountRepository = accountRepository;
    }

    @Override
    public void activate() {
        final var account = getAccount.execute();

        if (account.active()) {
            throw new AccountAlreadyEnableDisableException(String.format("Account %s is already enable", account.account()));
        }

        accountRepository.save(account.enable());
    }

    @Override
    public void deactivate() {
        final var account = getAccount.execute();

        if (!account.active()) {
            throw new AccountAlreadyEnableDisableException(String.format("Account %s is already disabled", account.account()));
        }

        accountRepository.save(account.disable());
    }
}
