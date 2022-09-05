package com.bank.transfers.application.infra.kafka;

import com.bank.transfers.application.app.messages.ISendBankTransferMessage;
import com.bank.transfers.application.domains.Transfer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BankTransferProducer implements ISendBankTransferMessage {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final static String SEND_BANK_TRANSFER = "send.bank.transfer";

    public BankTransferProducer(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(final Transfer transfer) {
        final var message = getObjectAsJson(transfer);
        kafkaTemplate.send(SEND_BANK_TRANSFER, message);
    }

    private String getObjectAsJson(final Transfer transfer) {
        try {
            return objectMapper.writeValueAsString(transfer);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Error convert object to json");
        }
    }
}
