package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.BankAccountNotActiveException;
import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ICashDepositRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IAddCashDeposit;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.domains.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AddCashDeposit implements IAddCashDeposit {

    private final IGetUserToken getUserToken;
    private final ICashDepositRepository cashDepositRepository;
    private final IAccountRepository accountRepository;

    public AddCashDeposit(final IGetUserToken getUserToken, final ICashDepositRepository cashDepositRepository, final IAccountRepository accountRepository) {
        this.getUserToken = getUserToken;
        this.cashDepositRepository = cashDepositRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public CashDeposit execute(final BigDecimal value) {
        final var user = this.getUserToken.execute();
        final var account = this.getAccount(user);
        final var cashDeposit = this.saveCashDeposit(user, account, value);
        return this.cashDepositRepository.save(cashDeposit);
    }

    private CashDeposit saveCashDeposit(final User user, final Account account, final BigDecimal value) {
        final var cashDeposit = CashDeposit.of(user,value);
        final var accountToSave = account.of(cashDeposit);
        this.accountRepository.save(accountToSave);
        return cashDeposit;
    }

    private Account getAccount(final User user) {
        final var account = accountRepository.findByUserId(user.id())
                .orElseThrow(() -> {
                    throw new BankAccountNotFoundException(String.format("User %s don't have a bank account", user.nameComplete()));
                });

        if (!account.active()) {
            throw new BankAccountNotActiveException(String.format("User %s don't have a bank account active", user.nameComplete()));
        }
        return account;
    }
}
