package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.IGetBalance;
import com.bank.transfers.application.domains.Balance;
import com.bank.transfers.application.infra.http.responses.BalanceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("balance")
public class GetBalanceController {

    private final IGetBalance getBalance;

    public GetBalanceController(final IGetBalance getBalance) {
        this.getBalance = getBalance;
    }

    @GetMapping
    public BalanceResponse execute() {
        final var response = getBalance.execute();
        return BalanceResponse.from(response);
    }
}
