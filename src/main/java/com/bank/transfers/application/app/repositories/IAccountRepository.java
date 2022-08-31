package com.bank.transfers.application.app.repositories;

import com.bank.transfers.application.domains.Account;

import java.util.Optional;

public interface IAccountRepository {
    Optional<Account> findByUserId(final String userId);

    Account save(final Account account);
}
