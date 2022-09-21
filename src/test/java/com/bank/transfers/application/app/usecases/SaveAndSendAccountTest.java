package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.messages.ISendMessage;
import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.app.repositories.ITransferRepository;
import com.bank.transfers.application.app.usecases.impl.SaveAndSendAccount;
import com.bank.transfers.application.domains.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SaveAndSendAccountTest {

    @InjectMocks
    private SaveAndSendAccount saveAndSendAccount;
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private ITransferRepository bankTransferRepository;
    @Mock
    private ISendMessage sendBankTransferMessage;
    @Mock
    private ObjectMapper objectMapper;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    public void testSaveAndSend() {

        final var objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        ReflectionTestUtils.setField(saveAndSendAccount, "objectMapper", objectMapper);
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");

        final var transfer = Transfer.of("456123", BigDecimal.valueOf(600));
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));

        when(bankTransferRepository.save(any())).thenReturn(transfer.withAccount(account));

        final var result = saveAndSendAccount.execute(transfer, account);
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(600));

        verify(sendBankTransferMessage).execute(anyString(), anyString());

    }

    @Test(expected = RuntimeException.class)
    public void testSaveAndSendThrowRuntimeException() throws JsonProcessingException {
        final var user = User.of("1", "Leonardo Dantas", "12356547987", "user@mail.com");

        final var transfer = Transfer.of("456123", BigDecimal.valueOf(600));
        final var account = Account.from("1", "1", "456123", "8", LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 3, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0), LocalDateTime.of(2021, 10, 5, 10, 30, 0)).withDeposit(CashDeposit.of(user, BigDecimal.valueOf(100), TransferType.USER_DEPOSIT));

        when(objectMapper.writeValueAsString(any()))
                .thenThrow(JsonProcessingException.class);

        saveAndSendAccount.execute(transfer, account);
    }
}
