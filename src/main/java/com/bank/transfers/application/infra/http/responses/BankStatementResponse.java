package com.bank.transfers.application.infra.http.responses;

import com.bank.transfers.application.domains.BankStatement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankStatementResponse(
        BigDecimal value,
        BankStatementTypeResponse type,
        LocalDateTime time
) {
    public static BankStatementResponse from(final BankStatement bankStatement) {
        return new BankStatementResponse(bankStatement.value(), BankStatementTypeResponse.valueOf(bankStatement.type().name()), bankStatement.time());
    }
}
