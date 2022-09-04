package com.bank.transfers.application.domains;


public record User(String id, String nameComplete, String document, String email, String password) {

    public static User of(final String id, final String name, final String document, final String email) {
        return new User(id, name, document, email, "");
    }

    public String documentOnlyNumbers() {
        return document;
    }

    public User updatePassword(final String passwordEncode) {
        return new User(this.id, this.nameComplete, this.document, this.email, passwordEncode);
    }

    public User cleanPassword() {
        return new User(this.id, this.nameComplete, this.document, this.email, "");
    }


}
