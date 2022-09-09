package com.bank.transfers.application.infra.http.controllers;

import com.bank.transfers.application.app.usecases.ICreateUser;
import com.bank.transfers.application.infra.http.converters.UserConverter;
import com.bank.transfers.application.infra.http.requests.UserRequest;
import com.bank.transfers.application.infra.http.responses.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("create/user")
public class CreateUserController {

    private final ICreateUser createUser;

    public CreateUserController(final ICreateUser createUser) {
        this.createUser = createUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse execute(@RequestBody @Valid final UserRequest request) {
        final var response = this.createUser.execute(UserConverter.toDomain(request));
        return UserResponse.from(response);
    }
}
