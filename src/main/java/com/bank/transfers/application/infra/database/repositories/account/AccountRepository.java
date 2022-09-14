package com.bank.transfers.application.infra.database.repositories.account;

import com.bank.transfers.application.app.repositories.IAccountRepository;
import com.bank.transfers.application.domains.Account;
import com.bank.transfers.application.infra.database.converters.AccountDocumentConverter;
import com.bank.transfers.application.infra.database.documents.AccountDocument;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountRepository implements IAccountRepository {

    private final AccountSpringData accountSpringData;

    public AccountRepository(final AccountSpringData accountSpringData) {
        this.accountSpringData = accountSpringData;
    }

    @Override
    public Optional<Account> findByUserId(final String userId) {
        return accountSpringData.findByUserId(userId).map(AccountDocumentConverter::toDomain);
    }

    @Override
    public Account save(final Account accountToSave) {
        try {
            final var accountDocument = AccountDocument.from(accountToSave);
            final var accountSave = accountSpringData.save(accountDocument);
            final var account = AccountDocumentConverter.toDomain(accountSave);
            return account;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Account> findByAccount(final String account) {
        return accountSpringData.findByAccount(account).map(AccountDocumentConverter::toDomain);
    }
}
