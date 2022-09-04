package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.app.exceptions.LogisticsTransferException;
import com.bank.transfers.application.app.exceptions.WithoutBalanceException;
import com.bank.transfers.application.app.messages.ISendBankTransferMessage;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.IBankTransferRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.app.usecases.impl.BankTransfer;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.domains.User;
import com.bank.transfers.application.infra.http.converters.TransferConverter;
import com.bank.transfers.application.infra.http.requests.TransferRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BankTransferTest {

    @InjectMocks
    private BankTransfer bankTransfer;

    @Mock
    private IGetUserToken getUserToken;

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private IBankTransferRepository bankTransferRepository;

    @Mock
    private ISendBankTransferMessage sendBankTransferMessage;

    @Test
    public void shouldTransferSuccessfully() {
        final var transferRequest = new TransferRequest("12315", BigDecimal.valueOf(100));

        final var user = User.of("1", "Leonardo Dantas", "86779775037", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(500));
        final var account = Account.of("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(cashDeposit);

        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.of(account));

        final var transfer = TransferConverter.toDomain(transferRequest);
        when(bankTransferRepository.save(any()))
                .thenReturn(transfer.withAccount(account));

        final var transferDetails = bankTransfer.execute(transfer);

        assertThat(transferDetails).isNotNull();
        assertThat(transferDetails.to()).isEqualTo(account.account());
        assertThat(transferDetails.from()).isEqualTo(transferRequest.from());
        assertThat(transferDetails.value()).isEqualTo(transferRequest.value());

        verify(bankTransferRepository, times(1)).save(any());
        verify(bankTransferRepository, times(1)).save(transfer.withAccount(account));
    }

    @Test(expected = LogisticsTransferException.class)
    public void shouldThrowLogisticsTransferException() {
        final var transfer = new TransferRequest("12315", BigDecimal.valueOf(100));

        final var user = User.of("1", "Leonardo Dantas", "96357358000145", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        final var account = Account.of("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0));

        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.of(account));

        bankTransfer.execute(TransferConverter.toDomain(transfer));
    }

    @Test(expected = BankAccountNotFoundException.class)
    public void shouldThrowBankAccountNotFoundException() {
        final var transfer = new TransferRequest("12315", BigDecimal.valueOf(100));

        final var user = User.of("1", "Leonardo Dantas", "96357358000145", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        when(accountRepository.findByUserId(any()))
                .thenThrow(BankAccountNotFoundException.class);

        bankTransfer.execute(TransferConverter.toDomain(transfer));
    }

    @Test(expected = WithoutBalanceException.class)
    public void shouldThrowWithoutBalanceException() {
        final var transfer = new TransferRequest("12315", BigDecimal.valueOf(600));

        final var user = User.of("1", "Leonardo Dantas", "86779775037", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(500));
        final var account = Account.of("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(cashDeposit);

        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.of(account));

        bankTransfer.execute(TransferConverter.toDomain(transfer));
    }

    @Test
    public void shouldSendMessage() {
        final var transfer = new TransferRequest("12315", BigDecimal.valueOf(100));

        final var user = User.of("1", "Leonardo Dantas", "86779775037", "user@email.com");
        when(getUserToken.execute()).thenReturn(user);

        final var cashDeposit = CashDeposit.of(user, BigDecimal.valueOf(500));
        final var account = Account.of("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(cashDeposit);

        when(accountRepository.findByUserId(user.id()))
                .thenReturn(Optional.of(account));

        bankTransfer.execute(TransferConverter.toDomain(transfer));

        verify(sendBankTransferMessage, times(1)).execute(any());
    }
}
