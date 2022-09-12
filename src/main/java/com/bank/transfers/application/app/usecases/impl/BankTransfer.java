package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.*;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ICashDepositRepository;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IBankTransfer;
import com.bank.transfers.application.app.usecases.ISaveAndSendAccount;
import com.bank.transfers.application.app.usecases.ITransferMoney;
import com.bank.transfers.application.domains.*;
import org.springframework.stereotype.Service;

@Service
public class BankTransfer implements IBankTransfer {

    private final IGetUserToken getUserToken;
    private final IAccountRepository accountRepository;
    private final ISaveAndSendAccount saveAndSendAccount;
    private final ITransferMoney transferMoney;

    public BankTransfer(final IGetUserToken getUserToken, final IAccountRepository accountRepository, final ISaveAndSendAccount saveAndSendAccount, final ITransferMoney transferMoney) {
        this.getUserToken = getUserToken;
        this.accountRepository = accountRepository;
        this.saveAndSendAccount = saveAndSendAccount;
        this.transferMoney = transferMoney;
    }

    @Override
    public Transfer execute(final Transfer transfer) {
        final var user = getUserToken.execute();
        final var account = getAccount(user);
        validateTransfer(user);
        validateBalance(transfer, account);
        final var cashWithdrawal = CashWithdrawal.of(user, transfer.value());
        transferMoney.execute(transfer);
        return saveAndSendAccount.execute(transfer, account.withWithdrawal(cashWithdrawal));
    }

    private Account getAccount(final User user) {
        final var account = accountRepository.findByUserId(user.id())
                .orElseThrow(() -> new BankAccountNotFoundException(String.format("User %s don't have an account", user.nameComplete())));

        if (account.active()) {
            return account;
        }

        throw new BankAccountNotActiveException(String.format("User %s don't have an active account", user.nameComplete()));
    }

    private void validateBalance(final Transfer transfer, final Account account) {
        if (account.amount().compareTo(transfer.value()) <= 0) {
            throw new WithoutBalanceException("User without balance");
        }
    }

    private void validateTransfer(final User user) {
        if (user.documentOnlyNumbers().length() == 14) {
            throw new LogisticsTransferException("Merchants cannot make transfers");
        }
    }
}
