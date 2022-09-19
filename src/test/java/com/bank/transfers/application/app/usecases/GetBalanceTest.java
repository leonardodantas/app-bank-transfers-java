package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.GetBalance;
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
public class GetBalanceTest {

    @InjectMocks
    private GetBalance getBalance;
    @Mock
    private IGetUserToken getUserToken;
    @Mock
    private IGetAccount getAccount;

    @Test
    public void testGetBalance() {

        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");

        when(getUserToken.execute())
                .thenReturn(user);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));
        when(getAccount.execute())
                .thenReturn(account);

        final var balance = getBalance.execute();
        assertThat(balance).isNotNull();
        assertThat(balance.value()).isEqualTo(BigDecimal.valueOf(100));

    }
}
