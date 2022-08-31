package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.BankAccountNotActiveException;
import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ICashDepositRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.AddCashDeposit;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.domains.CashWithdrawal;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddCashDepositTest {

    @InjectMocks
    private AddCashDeposit addCashDeposit;

    @Mock
    private IGetUserToken getUserToken;

    @Mock
    private ICashDepositRepository cashDepositRepository;

    @Mock
    private IAccountRepository accountRepository;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    public void addCashDeposit() {
        final var value = BigDecimal.valueOf(100);

        final var user = new User("1", "Leonardo Dantas", "12356547987", "user@mail.com", "");

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(value, user);

        when(cashDepositRepository.save(any()))
                .thenReturn(Optional.of(cashDepositSave));

        final var account = new Account("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), true, List.of(cashDepositSave), List.of(), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        final var cashDeposit = addCashDeposit.execute(value);

        assertThat(cashDeposit).isPresent();
        assertThat(cashDeposit.get().value()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(cashDeposit.get().userId()).isEqualTo(user.id());
    }

    @Test
    public void shouldUpdateAccount() {
        final var value = BigDecimal.valueOf(100);

        final var user = new User("1", "Leonardo Dantas", "12356547987", "user@mail.com", "");

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(value, user);

        when(cashDepositRepository.save(any()))
                .thenReturn(Optional.of(cashDepositSave));

        final var account = new Account("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), true, List.of(), List.of(), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        addCashDeposit.execute(value);

        verify(accountRepository, times(1)).findByUserId(user.id());
        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.cashDeposits().size()).isEqualTo(1);
        assertThat(result.fistDeposit()).isBefore(LocalDateTime.now());
        assertThat(result.lastDeposit()).isBefore(LocalDateTime.now());
    }

    @Test
    public void shouldUpdateAnAccountThatAlreadyHasADeposit() {
        final var value = BigDecimal.valueOf(100);

        final var user = new User("1", "Leonardo Dantas", "12356547987", "user@mail.com", "");

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(value, user);

        when(cashDepositRepository.save(any()))
                .thenReturn(Optional.of(cashDepositSave));

        final var account = new Account("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), true, List.of(cashDepositSave), List.of(), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        addCashDeposit.execute(value);

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.fistDeposit()).isEqualTo(LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        assertThat(result.lastDeposit()).isAfter(LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        assertThat(result.cashDeposits().size()).isEqualTo(2);
    }

    @Test(expected = BankAccountNotActiveException.class)
    public void shouldThrowBankAccountNotActiveException() {
        final var value = BigDecimal.valueOf(100);

        final var user = new User("1", "Leonardo Dantas", "12356547987", "user@mail.com", "");

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(value, user);

        final var account = new Account("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), false, List.of(cashDepositSave), List.of(), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        addCashDeposit.execute(value);
    }

    @Test(expected = BankAccountNotFoundException.class)
    public void shouldThrowBankAccountNotFoundException() {
        final var value = BigDecimal.valueOf(100);

        final var user = new User("1", "Leonardo Dantas", "12356547987", "user@mail.com", "");

        when(getUserToken.execute())
                .thenReturn(user);

        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.empty());

        addCashDeposit.execute(value);
    }

    @Test
    public void shouldReturnCorrectAmount() {
        final var value = BigDecimal.valueOf(200);

        final var user = new User("1", "Leonardo Dantas", "12356547987", "user@mail.com", "");

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(value, user);

        when(cashDepositRepository.save(any()))
                .thenReturn(Optional.of(cashDepositSave));

        final var account = new Account("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), true, List.of(cashDepositSave), List.of(), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        addCashDeposit.execute(value);

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(400));
    }

    @Test
    public void shouldReturnCorrectAmountWhenWithdrawalIsAlreadyMade() {
        final var value = BigDecimal.valueOf(200);

        final var user = new User("1", "Leonardo Dantas", "12356547987", "user@mail.com", "");

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(value, user);

        when(cashDepositRepository.save(any()))
                .thenReturn(Optional.of(cashDepositSave));

        final var cashWithdrawal = CashWithdrawal.of(BigDecimal.valueOf(50), user);

        final var account = new Account("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), true, List.of(cashDepositSave), List.of(cashWithdrawal), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        addCashDeposit.execute(value);

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(350));
    }

    @Test
    public void shouldReturnCorrectAmountWhenMoreThanOneLootExists() {
        final var value = BigDecimal.valueOf(200);

        final var user = new User("1", "Leonardo Dantas", "12356547987", "user@mail.com", "");

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(value, user);

        when(cashDepositRepository.save(any()))
                .thenReturn(Optional.of(cashDepositSave));

        final var cashWithdrawal = CashWithdrawal.of(BigDecimal.valueOf(50), user);

        final var account = new Account("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), true, List.of(cashDepositSave), List.of(cashWithdrawal, cashWithdrawal), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(accountRepository.findByUserId(any()))
                .thenReturn(Optional.of(account));

        addCashDeposit.execute(value);

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(300));
    }
}
