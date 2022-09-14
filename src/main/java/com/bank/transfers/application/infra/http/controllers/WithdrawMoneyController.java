package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.IWithdrawMoney;
import com.bank.transfers.application.infra.http.responses.CashWithdrawalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("withdraw")
public class WithdrawMoneyController {

    private final IWithdrawMoney withdrawMoney;

    public WithdrawMoneyController(final IWithdrawMoney withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    @PostMapping("/{value}")
    @ResponseStatus(HttpStatus.OK)
    public CashWithdrawalResponse execute(@PathVariable final BigDecimal value) {
        final var response = withdrawMoney.execute(value);
        return CashWithdrawalResponse.from(response);
    }
}
