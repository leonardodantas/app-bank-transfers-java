package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.IAddCashDeposit;
import com.bank.transfers.application.domains.CashDeposit;
import com.bank.transfers.application.infra.http.responses.CashDepositResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("deposit")
public class AddCashDepositController {

    private final IAddCashDeposit addCashDeposit;

    public AddCashDepositController(final IAddCashDeposit addCashDeposit) {
        this.addCashDeposit = addCashDeposit;
    }

    @PostMapping("/{value}")
    @ResponseStatus(HttpStatus.OK)
    public CashDepositResponse execute(@PathVariable final BigDecimal value){
        final var response = addCashDeposit.execute(value);
        return CashDepositResponse.from(response);
    }
}
