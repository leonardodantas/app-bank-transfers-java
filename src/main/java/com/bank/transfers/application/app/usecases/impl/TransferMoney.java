package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.exceptions.UserNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ICashDepositRepository;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.app.usecases.ITransferMoney;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.domains.Transfer;
import com.bank.transfers.application.domains.TransferType;
import org.springframework.stereotype.Service;

@Service
public class TransferMoney implements ITransferMoney {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;
    private final ICashDepositRepository cashDepositRepository;

    public TransferMoney(final IAccountRepository accountRepository, final IUserRepository userRepository, final ICashDepositRepository cashDepositRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.cashDepositRepository = cashDepositRepository;
    }

    @Override
    public void execute(final Transfer transfer) {
        final var destinationAccount = accountRepository.findByAccount(transfer.from())
                .orElseThrow(() -> new BankAccountNotFoundException(String.format("Account %s not found", transfer.to())));

        final var destinationUser = userRepository.findById(destinationAccount.userId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", destinationAccount.userId())));

        final var cashDeposit = CashDeposit.of(destinationUser, transfer.value(), TransferType.TRANSFER_FROM_ANOTHER_ACCOUNT);
        final var destinationAccountUpdate = destinationAccount.from(cashDeposit);
        accountRepository.save(destinationAccountUpdate);
        cashDepositRepository.save(cashDeposit);
    }
}
