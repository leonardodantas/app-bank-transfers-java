package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.IWithdrawMoney;
import com.bank.transfers.application.infra.http.responses.CashWithdrawalResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("withdraw")
public class WithdrawMoneyController {

    private final IWithdrawMoney withdrawMoney;

    public WithdrawMoneyController(final IWithdrawMoney withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    @PostMapping("/{value}")
    public CashWithdrawalResponse execute(@PathVariable final BigDecimal value) {
        final var response = withdrawMoney.execute(value);
        return CashWithdrawalResponse.from(response);
    }
}
