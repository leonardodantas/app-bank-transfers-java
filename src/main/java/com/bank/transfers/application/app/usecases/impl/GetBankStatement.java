package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IGetBankStatement;
import com.bank.transfers.application.domains.*;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Collection;
import java.util.stream.Stream;

@Service
public class GetBankStatement implements IGetBankStatement {

    private final IGetUserToken getUserToken;
    private final IAccountRepository accountRepository;

    public GetBankStatement(final IGetUserToken getUserToken, final IAccountRepository accountRepository) {
        this.getUserToken = getUserToken;
        this.accountRepository = accountRepository;
    }

    @Override
    public Collection<BankStatement> execute() {
        /**
         * Sort pela data e melhorar sequencia dos atributos da classe
         */
        final var user = getUserToken.execute();
        final var account = accountRepository.findByUserId(user.id())
                .orElseThrow(() -> new BankAccountNotFoundException(String.format("User %s don't have a bank account active", user.nameComplete())));

        final var deposits = getDeposits(account);
        final var withdrawal = getWithdrawal(account);

        return Stream.concat(deposits, withdrawal).toList();
    }

    private Stream<BankStatement> getWithdrawal(final Account account) {
        return account.cashWithdrawal()
                .stream().map(cashWithdrawal -> {
                    if(cashWithdrawal.type().equals(WithdrawalType.USER_WITHDRAWAL)) {
                        return BankStatement.of(cashWithdrawal.value(), BankStatementType.WITHDRAW);
                    }
                    return BankStatement.of(cashWithdrawal.value(), BankStatementType.TRANSFER);
                });
    }

    private Stream<BankStatement> getDeposits(final Account account) {
        return account.cashDeposits()
                .stream().map(cashDeposit -> BankStatement.of(cashDeposit.value(), BankStatementType.DEPOSIT));
    }
}
