package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.WithoutBalanceException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IGetAccount;
import com.bank.transfers.application.app.usecases.IWithdrawMoney;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.CashWithdrawal;
import com.bank.transfers.application.domains.User;
import com.bank.transfers.application.domains.WithdrawalType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawMoney implements IWithdrawMoney {

    private final IGetUserToken getUserToken;
    private final IAccountRepository accountRepository;
    private final IGetAccount getAccount;

    public WithdrawMoney(final IGetUserToken getUserToken, final IAccountRepository accountRepository, final IGetAccount getAccount) {
        this.getUserToken = getUserToken;
        this.accountRepository = accountRepository;
        this.getAccount = getAccount;
    }

    @Override
    public CashWithdrawal execute(final BigDecimal value) {
        final var user = getUserToken.execute();
        final var account = getAccount.execute();
        validateBalance(user, account, value);
        return saveAccountWithCashWithdrawal(user, account, value);
    }

    private CashWithdrawal saveAccountWithCashWithdrawal(final User user, final Account account, final BigDecimal value) {
        final var cashWithdrawal = CashWithdrawal.of(user, value, WithdrawalType.USER_WITHDRAWAL);
        final var accountUpdate = account.withWithdrawal(cashWithdrawal);
        accountRepository.save(accountUpdate);
        return cashWithdrawal;
    }

    private void validateBalance(final User user, final Account account, final BigDecimal value) {
        if (account.amount().compareTo(value) < 0) {
            throw new WithoutBalanceException(String.format("User %s has no balance", user.nameComplete()));
        }
    }
}
