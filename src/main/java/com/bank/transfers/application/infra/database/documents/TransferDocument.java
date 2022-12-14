package com.bank.transfers.application.infra.database.documents;

import com.bank.transfers.application.domains.Transfer;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("transfers")
public record TransferDocument(
        String id,
        String to,
        String from,
        BigDecimal value,
        LocalDateTime date) {

    public static TransferDocument from(final Transfer transfer) {
        return new TransferDocument(transfer.id(), transfer.to(), transfer.from(), transfer.value(), transfer.date());
    }
}
