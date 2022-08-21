package com.bank.transfers.application.domains;

public record User(String nameComplete, String document, String email, String password) {

    public String documentOnlyNumbers() {
        return document;
    }
}
