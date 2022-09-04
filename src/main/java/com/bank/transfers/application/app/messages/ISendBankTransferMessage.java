package com.bank.transfers.application.app.messages;

import com.bank.transfers.application.domains.Transfer;

public interface ISendBankTransferMessage {
    void execute(final Transfer transfer);
}
