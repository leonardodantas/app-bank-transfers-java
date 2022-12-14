package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ICashDepositRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.AddCashDeposit;
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

    @Mock
    private IGetAccount getAccount;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    public void addCashDeposit() {
        final var value = BigDecimal.valueOf(100);

        final var user = getUser();

        when(getUserToken.execute())
                .thenReturn(user);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(getAccount.execute())
                .thenReturn(account);

        final var cashDepositSave = CashDeposit.of(user, value, TransferType.USER_DEPOSIT);

        when(cashDepositRepository.save(any()))
                .thenReturn(cashDepositSave);

        final var cashDeposit = addCashDeposit.execute(value);

        assertThat(cashDeposit).isNotNull();
        assertThat(cashDeposit.value()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(cashDeposit.userId()).isEqualTo(user.id());
    }

    @Test
    public void shouldUpdateAccount() {
        final var value = BigDecimal.valueOf(100);

        final var user = getUser();

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(user, value, TransferType.USER_DEPOSIT);

        when(cashDepositRepository.save(any()))
                .thenReturn(cashDepositSave);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(getAccount.execute())
                .thenReturn(account);

        final var cashDeposit = addCashDeposit.execute(value);

        assertThat(cashDeposit).isNotNull();

        verify(getAccount, times(1)).execute();
        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();

        assertThat(result.cashDeposits().size()).isEqualTo(1);
        assertThat(result.fistDeposit()).isBefore(LocalDateTime.now());
        assertThat(result.lastDeposit()).isBefore(LocalDateTime.now());
    }

    @Test
    public void shouldUpdateAnAccountThatAlreadyHasADeposit() {
        final var value = BigDecimal.valueOf(100);

        final var user = getUser();

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(user, value, TransferType.USER_DEPOSIT);

        when(cashDepositRepository.save(any()))
                .thenReturn(cashDepositSave);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(cashDepositSave);
        when(getAccount.execute())
                .thenReturn(account);

        final var cashDeposit = addCashDeposit.execute(value);

        assertThat(cashDeposit).isNotNull();

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.fistDeposit()).isEqualTo(LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        assertThat(result.lastDeposit()).isAfter(LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        assertThat(result.cashDeposits().size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnCorrectAmount() {
        final var value = BigDecimal.valueOf(200);

        final var user = getUser();

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(user, value, TransferType.USER_DEPOSIT);

        when(cashDepositRepository.save(any()))
                .thenReturn(cashDepositSave);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(cashDepositSave);
        when(getAccount.execute())
                .thenReturn(account);

        final var cashDeposit = addCashDeposit.execute(value);

        assertThat(cashDeposit).isNotNull();

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(400));
    }

    @Test
    public void shouldReturnCorrectAmountWhenWithdrawalIsAlreadyMade() {
        final var value = BigDecimal.valueOf(200);

        final var user = getUser();

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(user, value, TransferType.USER_DEPOSIT);

        when(cashDepositRepository.save(any()))
                .thenReturn(cashDepositSave);

        final var cashWithdrawal = CashWithdrawal.of(user, BigDecimal.valueOf(50), WithdrawalType.USER_WITHDRAWAL);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withBankTransactions(BankTransactions.of(cashDepositSave, cashWithdrawal));
        when(getAccount.execute())
                .thenReturn(account);

        final var cashDeposit = addCashDeposit.execute(value);

        assertThat(cashDeposit).isNotNull();

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(350));
    }

    @Test
    public void shouldReturnCorrectAmountWhenMoreThanOneLootExists() {
        final var value = BigDecimal.valueOf(200);

        final var user = getUser();

        when(getUserToken.execute())
                .thenReturn(user);

        final var cashDepositSave = CashDeposit.of(user, value, TransferType.USER_DEPOSIT);

        when(cashDepositRepository.save(any()))
                .thenReturn(cashDepositSave);

        final var cashWithdrawal = CashWithdrawal.of(user, BigDecimal.valueOf(100), WithdrawalType.USER_WITHDRAWAL);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withBankTransactions(BankTransactions.of(List.of(cashDepositSave), List.of(cashWithdrawal)));
        when(getAccount.execute())
                .thenReturn(account);

        final var cashDeposit = addCashDeposit.execute(value);

        assertThat(cashDeposit).isNotNull();

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

        final var result = accountArgumentCaptor.getValue();
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(300));
    }

    private User getUser() {
        return User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
    }
}
