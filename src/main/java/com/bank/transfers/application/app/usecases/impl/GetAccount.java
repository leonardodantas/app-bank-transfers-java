package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.BankAccountNotActiveException;
import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IGetAccount;
import com.bank.transfers.application.domains.Account;
import org.springframework.stereotype.Service;

@Service
public class GetAccount implements IGetAccount {

    private final IGetUserToken getUserToken;
    private final IAccountRepository accountRepository;

    public GetAccount(final IGetUserToken getUserToken, final IAccountRepository accountRepository) {
        this.getUserToken = getUserToken;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account execute() {
        final var user = getUserToken.execute();
        final var account = accountRepository.findByUserId(user.id())
                .orElseThrow(() -> new BankAccountNotFoundException(String.format("User id %s don't have a bank account", user.id())));

        validateAccountIsActive(account);

        return account;
    }

    @Override
    public Account execute(final String accountNumber) {
        final var account = accountRepository.findByAccount(accountNumber)
                .orElseThrow(() -> new BankAccountNotFoundException(String.format("Account %s not found", accountNumber)));

        validateAccountIsActive(account);

        return account;
    }

    private void validateAccountIsActive(final Account account) {
        if (account.active()) {
            throw new BankAccountNotActiveException(String.format("Account %s disable", account.account()));
        }
    }
}
