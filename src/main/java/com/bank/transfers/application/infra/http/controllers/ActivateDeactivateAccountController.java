package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.IActivateDeactivateAccount;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("status")
public class ActivateDeactivateAccountController {

    private final IActivateDeactivateAccount activateDeactivateAccount;

    public ActivateDeactivateAccountController(final IActivateDeactivateAccount activateDeactivateAccount) {
        this.activateDeactivateAccount = activateDeactivateAccount;
    }

    @PutMapping("enable")
    @ResponseStatus(HttpStatus.OK)
    public void activate(){
        activateDeactivateAccount.activate();
    }

    @PutMapping("disable")
    @ResponseStatus(HttpStatus.OK)
    public void deactivate(){
        activateDeactivateAccount.deactivate();
    }
}
