package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.UserNotFoundException;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ICashDepositRepository;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.app.usecases.impl.TransferMoney;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransferMoneyTest {

    @InjectMocks
    private TransferMoney transferMoney;
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private ICashDepositRepository cashDepositRepository;
    @Mock
    private IGetAccount getAccount;
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;
    @Captor
    private ArgumentCaptor<CashDeposit> cashDepositArgumentCaptor;

    @Test
    public void testExecute() {
        final var userFrom = User.of("2", "Leonardo Rodrigues", "45687129750", "user@mail.com");
        final var accountFrom = Account.from("2", "2", "582159", "9", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(userFrom, BigDecimal.valueOf(10000), TransferType.USER_DEPOSIT));
        when(getAccount.execute())
                .thenReturn(accountFrom);

        final var userTo = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var accountTo = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(userTo, BigDecimal.valueOf(10000), TransferType.USER_DEPOSIT));
        when(getAccount.execute(anyString()))
                .thenReturn(accountTo);

        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(userTo));

        final var transfer = Transfer.of("4412765", BigDecimal.valueOf(1000));
        transferMoney.execute(transfer);

        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());
        verify(cashDepositRepository, times(1)).save(cashDepositArgumentCaptor.capture());

        final var accountCaptor = accountArgumentCaptor.getValue();
        assertThat(accountCaptor).isNotNull();

        final var cashDepositCaptor = cashDepositArgumentCaptor.getValue();
        assertThat(cashDepositCaptor).isNotNull();
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundException() {
        final var userFrom = User.of("2", "Leonardo Rodrigues", "45687129750", "user@mail.com");
        final var accountFrom = Account.from("2", "2", "582159", "9", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(userFrom, BigDecimal.valueOf(10000), TransferType.USER_DEPOSIT));
        when(getAccount.execute())
                .thenReturn(accountFrom);

        final var userTo = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");
        final var accountTo = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(userTo, BigDecimal.valueOf(10000), TransferType.USER_DEPOSIT));
        when(getAccount.execute(anyString()))
                .thenReturn(accountTo);

        when(userRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        final var transfer = Transfer.of("4412765", BigDecimal.valueOf(1000));
        transferMoney.execute(transfer);
    }
}
