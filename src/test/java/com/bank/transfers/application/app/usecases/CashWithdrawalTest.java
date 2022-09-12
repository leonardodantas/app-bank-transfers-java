package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.WithoutBalanceException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.WithdrawMoney;
import com.bank.transfers.application.domains.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CashWithdrawalTest {

    @InjectMocks
    private WithdrawMoney withdrawMoney;

    @Mock
    private IGetUserToken getUserToken;

    @Mock
    private IAccountRepository accountRepository;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    public void shouldMakeWithdrawal() {
        final var value = BigDecimal.valueOf(500);

        final var user = getUser();

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(600));
        final var cashWithdrawal = CashWithdrawal.of(user, BigDecimal.valueOf(100));

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withBankTransactions(BankTransactions.of(List.of(cashDeposit), List.of(cashWithdrawal)));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        final var result = withdrawMoney.execute(value);

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(value);

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());
        final var accountSave = accountArgumentCaptor.getValue();

        assertThat(accountSave.amount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test(expected = WithoutBalanceException.class)
    public void shouldThrowWithoutBalanceException() {
        final var value = BigDecimal.valueOf(500);

        final var user = getUser();

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(400));
        final var cashWithdrawal = CashWithdrawal.of(user, BigDecimal.valueOf(500));

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withBankTransactions(BankTransactions.of(List.of(cashDeposit), List.of(cashWithdrawal)));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        withdrawMoney.execute(value);
    }

    private User getUser() {
        return User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
    }


}
