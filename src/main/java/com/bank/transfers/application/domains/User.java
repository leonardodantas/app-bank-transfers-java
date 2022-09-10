package com.bank.transfers.application.domains;


public record User(
        String id,
        String nameComplete,
        String document,
        String documentOnlyNumbers,
        String email,
        String password) {

    public static User of(final String id, final String name, final String document, final String email) {
        final var documentOnlyNumbers = document.replaceAll("\\D", "");
        return new User(id, name, document, documentOnlyNumbers, email, "");
    }

    public User cleanPassword() {
        return new User(this.id, this.nameComplete, this.document, this.documentOnlyNumbers, this.email, "");
    }


}
