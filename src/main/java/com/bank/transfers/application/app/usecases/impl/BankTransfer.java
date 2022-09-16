package com.bank.transfers.application.app.usecases.impl;

import com.bank.transfers.application.app.exceptions.LogisticsTransferException;
import com.bank.transfers.application.app.exceptions.WithoutBalanceException;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.IBankTransfer;
import com.bank.transfers.application.app.usecases.IGetAccount;
import com.bank.transfers.application.app.usecases.ISaveAndSendAccount;
import com.bank.transfers.application.app.usecases.ITransferMoney;
import com.bank.transfers.application.domains.*;
import org.springframework.stereotype.Service;

@Service
public class BankTransfer implements IBankTransfer {

    private final IGetUserToken getUserToken;
    private final ISaveAndSendAccount saveAndSendAccount;
    private final ITransferMoney transferMoney;
    private final IGetAccount getAccount;

    public BankTransfer(final IGetUserToken getUserToken, final ISaveAndSendAccount saveAndSendAccount, final ITransferMoney transferMoney, final IGetAccount getAccount) {
        this.getUserToken = getUserToken;
        this.saveAndSendAccount = saveAndSendAccount;
        this.transferMoney = transferMoney;
        this.getAccount = getAccount;
    }

    @Override
    public Transfer execute(final Transfer transfer) {
        final var user = getUserToken.execute();
        final var account = getAccount.execute();
        validateTransfer(user);
        validateBalance(transfer, account);
        final var cashWithdrawal = CashWithdrawal.of(user, transfer.value(), WithdrawalType.TRANSFER_TO_ANOTHER_ACCOUNT);
        transferMoney.execute(transfer);
        return saveAndSendAccount.execute(transfer, account.withWithdrawal(cashWithdrawal));
    }

    private void validateBalance(final Transfer transfer, final Account account) {
        if (account.amount().compareTo(transfer.value()) < 0) {
            throw new WithoutBalanceException("User without balance");
        }
    }

    private void validateTransfer(final User user) {
        if (user.documentOnlyNumbers().length() == 14) {
            throw new LogisticsTransferException("Merchants cannot make transfers");
        }
    }
}
