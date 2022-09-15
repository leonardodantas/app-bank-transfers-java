package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.GetBankStatement;
import com.bank.transfers.application.domains.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetBankStatementTest {

    @InjectMocks
    private GetBankStatement getBankStatement;
    @Mock
    private IGetUserToken getUserToken;
    @Mock
    private IAccountRepository accountRepository;

    @Test
    public void getBankStatement() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(10000), TransferType.USER_DEPOSIT);
        final var cashDeposit01 = CashDeposit.of(user, BigDecimal.valueOf(500), TransferType.USER_DEPOSIT);
        final var cashDeposit02 = CashDeposit.of(user, BigDecimal.valueOf(3000), TransferType.TRANSFER_FROM_ANOTHER_ACCOUNT);
        final var cashDeposit03 = CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.TRANSFER_FROM_ANOTHER_ACCOUNT);

        final var cashDeposits = List.of(cashDeposit, cashDeposit01, cashDeposit02, cashDeposit03);

        final var cashWithdrawal = CashWithdrawal.of(user, BigDecimal.valueOf(500), WithdrawalType.USER_WITHDRAWAL);
        final var cashWithdrawal01 = CashWithdrawal.of(user, BigDecimal.valueOf(1500), WithdrawalType.USER_WITHDRAWAL);
        final var cashWithdrawal02 = CashWithdrawal.of(user, BigDecimal.valueOf(2500), WithdrawalType.TRANSFER_TO_ANOTHER_ACCOUNT);

        final var cashWithdrawals = List.of(cashWithdrawal, cashWithdrawal01, cashWithdrawal02);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withBankTransactions(BankTransactions.of(cashDeposits, cashWithdrawals));

        when(getUserToken.execute())
                .thenReturn(user);

        when(accountRepository.findByUserId(anyString()))
                .thenReturn(Optional.of(account));

        final var result = getBankStatement.execute();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(7);
    }

    @Test(expected = BankAccountNotFoundException.class)
    public void testThrowBankAccountNotFoundException() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");

        when(getUserToken.execute())
                .thenReturn(user);

        when(accountRepository.findByUserId(anyString()))
                .thenReturn(Optional.empty());

        getBankStatement.execute();
    }
}
