package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.AccountAlreadyEnableDisableException;
import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.ActivateDeactivateAccount;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.domains.TransferType;
import com.bank.transfers.application.domains.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivateDeactivateAccountTest {

    @InjectMocks
    private ActivateDeactivateAccount activateDeactivateAccount;
    @Mock
    private IGetAccount getAccount;
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private IGetUserToken getUserToken;
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    public void testActivate() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));

        when(getUserToken.execute())
                .thenReturn(user);
        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.of(account.disable()));

        activateDeactivateAccount.activate();

        verify(accountRepository).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();

        assertThat(result).isNotNull();
        assertThat(result.active()).isTrue();
    }

    @Test
    public void testDeactivate() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));

        when(getAccount.execute())
                .thenReturn(account.enable());

        activateDeactivateAccount.deactivate();

        verify(accountRepository).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();

        assertThat(result).isNotNull();
        assertThat(result.active()).isFalse();
    }

    @Test(expected = AccountAlreadyEnableDisableException.class)
    public void testAccountAlreadyEnableDisableException() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));

        when(getUserToken.execute())
                .thenReturn(user);
        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.of(account));

        activateDeactivateAccount.activate();
    }

    @Test(expected = BankAccountNotFoundException.class)
    public void testBankAccountNotFoundException() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");

        when(getUserToken.execute())
                .thenReturn(user);
        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.empty());

        activateDeactivateAccount.activate();
    }

}
