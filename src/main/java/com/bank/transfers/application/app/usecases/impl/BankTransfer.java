package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.LogisticsTransferException;
import com.bank.transfers.application.app.exceptions.WithoutBalanceException;
import com.bank.transfers.application.app.messages.ISendBankTransferMessage;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ITransferRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IBankTransfer;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.Transfer;
import com.bank.transfers.application.domains.User;
import org.springframework.stereotype.Service;

@Service
public class BankTransfer implements IBankTransfer {

    private final IGetUserToken getUserToken;
    private final IAccountRepository accountRepository;
    private final ITransferRepository bankTransferRepository;
    private final ISendBankTransferMessage sendBankTransferMessage;

    public BankTransfer(final IGetUserToken getUserToken, final IAccountRepository accountRepository, final ITransferRepository bankTransferRepository, final ISendBankTransferMessage sendBankTransferMessage) {
        this.getUserToken = getUserToken;
        this.accountRepository = accountRepository;
        this.bankTransferRepository = bankTransferRepository;
        this.sendBankTransferMessage = sendBankTransferMessage;
    }

    @Override
    public Transfer execute(final Transfer transfer) {
        final var user = getUserToken.execute();
        final var account = getAccount(user);
        validateTransfer(user);
        validateBalance(transfer, account);
        return saveAndSendMessage(transfer, account);
    }

    private Transfer saveAndSendMessage(final Transfer transfer, final Account account) {
        final var transferSave = bankTransferRepository.save(transfer.withAccount(account));
        sendBankTransferMessage.execute(transfer);
        return transferSave;
    }

    private Account getAccount(final User user) {
        return accountRepository.findByUserId(user.id())
                .orElseGet(() -> null);
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
