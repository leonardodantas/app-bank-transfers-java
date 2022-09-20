package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.BankAccountNotActiveException;
import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.GetAccount;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAccountTest {

    @InjectMocks
    private GetAccount getAccount;
    @Mock
    private IGetUserToken getUserToken;
    @Mock
    private IAccountRepository accountRepository;

    @Test
    public void testExecute() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));

        when(getUserToken.execute())
                .thenReturn(user);
        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.of(account.enable()));

        final var result = getAccount.execute();
        assertThat(result).isNotNull();
    }

    @Test
    public void testExecuteByAccountNumber() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));
        final var accountNumber = "9954121";

        when(accountRepository.findByAccount(accountNumber))
                .thenReturn(Optional.of(account.enable()));

        final var result = getAccount.execute(accountNumber);
        assertThat(result).isNotNull();
    }

    @Test(expected = BankAccountNotFoundException.class)
    public void testExecuteBankAccountNotFoundException() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        when(getUserToken.execute())
                .thenReturn(user);
        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.empty());

        getAccount.execute();
    }

    @Test(expected = BankAccountNotActiveException.class)
    public void testExecuteBankAccountNotActiveException() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));

        when(getUserToken.execute())
                .thenReturn(user);

        when(accountRepository.findByUserId(anyString()))
                .thenReturn(Optional.of(account.disable()));

        getAccount.execute();
    }

    @Test(expected = BankAccountNotFoundException.class)
    public void testExecuteByAccountNumberBankAccountNotFoundException() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var accountNumber = "987454";

        when(getUserToken.execute())
                .thenReturn(user);

        when(accountRepository.findByAccount(accountNumber))
                .thenReturn(Optional.empty());

        getAccount.execute(accountNumber);
    }

    @Test(expected = BankAccountNotActiveException.class)
    public void testExecuteByAccountNumberBankAccountNotActiveException() {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));

        final var accountNumber = "987454";

        when(getUserToken.execute())
                .thenReturn(user);

        when(accountRepository.findByAccount(accountNumber))
                .thenReturn(Optional.of(account.disable()));

        getAccount.execute(accountNumber);
    }
}
