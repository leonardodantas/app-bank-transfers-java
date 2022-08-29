package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.usecases.impl.AddCashDeposit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AddCashDepositTest {

    private AddCashDeposit addCashDeposit;

    @Test
    public void addCashDeposit() {
        final var value = BigDecimal.valueOf(100);
        final var cashDeposit = addCashDeposit.execute(value);

        assertThat(cashDeposit).isNotNull();
    }
}
