package com.bank.transfers.application.domains;

public class User {

    private final String nameComplete;
    private final String document;
    private final String email;

    private final String password;

    public User(final String nameComplete, final String document, final String email, final String password) {
        this.nameComplete = nameComplete;
        this.document = document;
        this.email = email;
        this.password = password;
    }


    public String getDocumentOnlyNumbers() {
        return document;
    }

    public String getDocument() {
        return document;
    }

    public String getEmail() {
        return email;
    }
}
