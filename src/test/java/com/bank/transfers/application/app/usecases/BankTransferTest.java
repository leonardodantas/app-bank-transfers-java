package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.LogisticsTransferException;
import com.bank.transfers.application.app.exceptions.WithoutBalanceException;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.BankTransfer;
import com.bank.transfers.application.domains.*;
import com.bank.transfers.application.infra.http.converters.TransferConverter;
import com.bank.transfers.application.infra.http.requests.TransferRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BankTransferTest {

    @InjectMocks
    private BankTransfer bankTransfer;
    @Mock
    private IGetUserToken getUserToken;
    @Mock
    private ITransferMoney transferMoney;
    @Mock
    private ISaveAndSendAccount saveAndSendAccount;
    @Mock
    private IGetAccount getAccount;
    @Captor
    private ArgumentCaptor<Transfer> transferArgumentCaptor;

    @Test
    public void shouldTransferSuccessfully() {
        final var transferRequest = new TransferRequest("12315", BigDecimal.valueOf(100));

        final var user = User.of("1", "Leonardo Dantas", "86779775037", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(500), TransferType.USER_DEPOSIT);
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(cashDeposit);
        when(getAccount.execute())
                .thenReturn(account);

        final var transfer = TransferConverter.toDomain(transferRequest);

        when(saveAndSendAccount.execute(any(), any()))
                .thenReturn(transfer);

        final var transferDetails = bankTransfer.execute(transfer);

        assertThat(transferDetails).isNotNull();
        assertThat(transferDetails.value()).isEqualTo(transferRequest.value());

        verify(transferMoney, times(1)).execute(transferArgumentCaptor.capture());
        final var transferCaptor = transferArgumentCaptor.getValue();
        assertThat(transferCaptor).isNotNull();
    }

    @Test(expected = LogisticsTransferException.class)
    public void shouldThrowLogisticsTransferException() {
        final var transfer = new TransferRequest("12315", BigDecimal.valueOf(100));

        final var user = User.of("1", "Leonardo Dantas", "96357358000145", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));
        when(getAccount.execute())
                .thenReturn(account);

        bankTransfer.execute(TransferConverter.toDomain(transfer));
    }

    @Test(expected = WithoutBalanceException.class)
    public void shouldThrowWithoutBalanceException() {
        final var transfer = new TransferRequest("12315", BigDecimal.valueOf(600));

        final var user = User.of("1", "Leonardo Dantas", "86779775037", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(500), TransferType.USER_DEPOSIT);
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(cashDeposit);

        when(getAccount.execute())
                .thenReturn(account);

        bankTransfer.execute(TransferConverter.toDomain(transfer));
    }

    @Test
    public void shouldSendMessage() {
        final var transfer = new TransferRequest("12315", BigDecimal.valueOf(100));

        final var user = User.of("1", "Leonardo Dantas", "86779775037", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(500), TransferType.USER_DEPOSIT);
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(cashDeposit);

        when(getAccount.execute())
                .thenReturn(account);

        bankTransfer.execute(TransferConverter.toDomain(transfer));

        verify(saveAndSendAccount, times(1)).execute(any(), any());
    }
}
