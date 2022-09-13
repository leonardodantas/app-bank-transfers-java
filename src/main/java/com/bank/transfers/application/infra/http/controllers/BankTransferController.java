package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.IBankTransfer;
import com.bank.transfers.application.infra.http.converters.TransferConverter;
import com.bank.transfers.application.infra.http.requests.TransferRequest;
import com.bank.transfers.application.infra.http.responses.TransferResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("bank/transfer")
public class BankTransferController {

    private final IBankTransfer bankTransfer;

    public BankTransferController(final IBankTransfer bankTransfer) {
        this.bankTransfer = bankTransfer;
    }

    @PostMapping
    public TransferResponse execute(@RequestBody @Valid final TransferRequest request) {
        final var response = bankTransfer.execute(TransferConverter.toDomain(request));
        return TransferResponse.from(response);
    }
}
