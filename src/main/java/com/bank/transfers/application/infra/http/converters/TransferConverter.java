package com.bank.transfers.application.infra.http.converters;

import com.bank.transfers.application.domains.Transfer;
import com.bank.transfers.application.infra.http.requests.TransferRequest;

public class TransferConverter {

    public static Transfer toDomain(final TransferRequest json) {
        return Transfer.of(json.to(), json.value());
    }
}
