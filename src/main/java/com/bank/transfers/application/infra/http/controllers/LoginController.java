package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.ILoginUser;
import com.bank.transfers.application.infra.http.requests.UserLoginRequest;
import com.bank.transfers.application.infra.http.responses.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("login")
public class LoginController {

    private final ILoginUser loginUser;

    public LoginController(final ILoginUser loginUser) {
        this.loginUser = loginUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse execute(@Valid @RequestBody final UserLoginRequest request){
        final var response = loginUser.execute(request.email(), request.password());
        return TokenResponse.from(response);
    }
}
