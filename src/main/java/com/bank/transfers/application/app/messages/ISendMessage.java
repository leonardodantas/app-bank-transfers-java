package com.bank.transfers.application.app.messages;


public interface ISendMessage {
    void execute(final String message, final String topic);
}
