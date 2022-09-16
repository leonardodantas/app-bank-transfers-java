package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ICashDepositRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IAddCashDeposit;
import com.bank.transfers.application.app.usecases.IGetAccount;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.domains.TransferType;
import com.bank.transfers.application.domains.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AddCashDeposit implements IAddCashDeposit {

    private final IGetUserToken getUserToken;
    private final ICashDepositRepository cashDepositRepository;
    private final IAccountRepository accountRepository;
    private final IGetAccount getAccount;

    public AddCashDeposit(final IGetUserToken getUserToken, final ICashDepositRepository cashDepositRepository, final IAccountRepository accountRepository, final IGetAccount getAccount) {
        this.getUserToken = getUserToken;
        this.cashDepositRepository = cashDepositRepository;
        this.accountRepository = accountRepository;
        this.getAccount = getAccount;
    }

    @Override
    public CashDeposit execute(final BigDecimal value) {
        final var user = this.getUserToken.execute();
        final var account = getAccount.execute();
        return this.saveCashDeposit(user, account, value);
    }

    private CashDeposit saveCashDeposit(final User user, final Account account, final BigDecimal value) {
        final var cashDeposit = CashDeposit.of(user, value, TransferType.USER_DEPOSIT);
        final var accountToSave = account.from(cashDeposit);
        this.accountRepository.save(accountToSave);
        return this.cashDepositRepository.save(cashDeposit);
    }
}
