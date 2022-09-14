package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.IGetBankStatement;
import com.bank.transfers.application.domains.BankStatement;
import com.bank.transfers.application.infra.http.responses.BankStatementResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("bank/statement")
public class BankStatementController {

    private final IGetBankStatement getBankStatement;

    public BankStatementController(final IGetBankStatement getBankStatement) {
        this.getBankStatement = getBankStatement;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BankStatementResponse> execute(){
        final var response = getBankStatement.execute();
        return response.stream().map(BankStatementResponse::from).toList();
    }
}
