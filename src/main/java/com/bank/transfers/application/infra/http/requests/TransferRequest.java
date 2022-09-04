package com.bank.transfers.application.infra.http.requests;

import java.math.BigDecimal;

public record TransferRequest (
        String from,
        BigDecimal value
){
}
