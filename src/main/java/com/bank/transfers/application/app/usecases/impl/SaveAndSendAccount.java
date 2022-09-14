package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.messages.ISendMessage;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ITransferRepository;
import com.bank.transfers.application.app.usecases.ISaveAndSendAccount;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.Transfer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class SaveAndSendAccount implements ISaveAndSendAccount {

    private final IAccountRepository accountRepository;
    private final ITransferRepository bankTransferRepository;
    private final ISendMessage sendBankTransferMessage;
    private final ObjectMapper objectMapper;
    private final static String SEND_BANK_TRANSFER = "send.bank.transfer";

    public SaveAndSendAccount(final IAccountRepository accountRepository, final ITransferRepository bankTransferRepository, final ISendMessage sendBankTransferMessage, final ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.bankTransferRepository = bankTransferRepository;
        this.sendBankTransferMessage = sendBankTransferMessage;
        this.objectMapper = objectMapper;
    }

    @Override
    public Transfer execute(final Transfer transfer, final Account account) {
        sendBankTransferMessage.execute(this.getObjectAsJson(transfer), SEND_BANK_TRANSFER);
        this.accountRepository.save(account);
        return bankTransferRepository.save(transfer.withAccount(account));
    }


    private String getObjectAsJson(final Transfer transfer) {
        try {
            return objectMapper.writeValueAsString(transfer);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Error convert object to json");
        }
    }
}
