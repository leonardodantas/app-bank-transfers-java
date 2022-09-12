package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.messages.ISendBankTransferMessage;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ITransferRepository;
import com.bank.transfers.application.app.usecases.ISaveAndSendAccount;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.Transfer;
import org.springframework.stereotype.Service;

@Service
public class SaveAndSendAccount implements ISaveAndSendAccount {

    private final IAccountRepository accountRepository;
    private final ITransferRepository bankTransferRepository;
    private final ISendBankTransferMessage sendBankTransferMessage;

    public SaveAndSendAccount(final IAccountRepository accountRepository, final ITransferRepository bankTransferRepository, final ISendBankTransferMessage sendBankTransferMessage) {
        this.accountRepository = accountRepository;
        this.bankTransferRepository = bankTransferRepository;
        this.sendBankTransferMessage = sendBankTransferMessage;
    }

    @Override
    public Transfer execute(final Transfer transfer, final Account account) {
        sendBankTransferMessage.execute(transfer);
        this.accountRepository.save(account);
        return bankTransferRepository.save(transfer.withAccount(account));
    }
}
