package com.bank.transfers.application.domains;


public record User(String id, String nameComplete, String document, String email, String password) {

    public String documentOnlyNumbers() {
        return document;
    }


        return new User(this.id, this.nameComplete, this.document, this.email, passwordEncode);
    }

    public User cleanPassword() {
        return new User(this.id, this.nameComplete, this.document, this.email, "");

    }

    public User cleanPassword(){
        return new User(this.nameComplete, this.document, this.email, "");
    }
}
