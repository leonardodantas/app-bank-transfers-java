package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.WithoutBalanceException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.WithdrawMoney;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.domains.TransferType;
import com.bank.transfers.application.domains.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WithdrawMoneyTest {

    @InjectMocks
    private WithdrawMoney withdrawMoney;
    @Mock
    private IGetUserToken getUserToken;
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private IGetAccount getAccount;

    @Test
    public void withdrawMoney() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(10000), TransferType.USER_DEPOSIT));

        when(getUserToken.execute())
                .thenReturn(user);

        when(getAccount.execute())
                .thenReturn(account);

        final var result = withdrawMoney.execute(BigDecimal.valueOf(1000));
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test(expected = WithoutBalanceException.class)
    public void shouldThrowWithoutBalanceException() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(10000), TransferType.USER_DEPOSIT));

        when(getUserToken.execute())
                .thenReturn(user);

        when(getAccount.execute())
                .thenReturn(account);

        withdrawMoney.execute(BigDecimal.valueOf(100000));
    }
}
