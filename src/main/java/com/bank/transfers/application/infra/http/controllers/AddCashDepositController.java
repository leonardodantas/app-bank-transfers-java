package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.IAddCashDeposit;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.infra.http.responses.CashDepositResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("deposit")
public class AddCashDepositController {

    private final IAddCashDeposit addCashDeposit;

    public AddCashDepositController(final IAddCashDeposit addCashDeposit) {
        this.addCashDeposit = addCashDeposit;
    }

    @PostMapping("/{value}")
    public CashDepositResponse execute(@PathVariable final BigDecimal value){
        final var response = addCashDeposit.execute(value);
        return CashDepositResponse.from(response);
    }
}
