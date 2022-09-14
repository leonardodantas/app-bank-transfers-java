package com.bank.transfers.application.infra.kafka;

import com.bank.transfers.application.app.messages.ISendMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer implements ISendMessage {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public Producer(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void execute(final String message, final String topic) {
        kafkaTemplate.send(topic, message);
    }

}
