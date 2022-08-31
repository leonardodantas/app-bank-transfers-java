package com.bank.transfers.application.infra.security;

import com.bank.transfers.application.app.exceptions.UserNotFoundException;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.app.security.IGetUserToken;
import com.bank.transfers.application.domains.User;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class GetUserToken implements IGetUserToken {

    private final String secret;
    private final HttpServletRequest httpServletRequest;
    private final IUserRepository userRepository;

    public GetUserToken(@Value("${jwt.secret}") final String secret, final HttpServletRequest httpServletRequest, final IUserRepository userRepository) {
        this.secret = secret;
        this.httpServletRequest = httpServletRequest;
        this.userRepository = userRepository;
    }

    @Override
    public User execute() {
        final var token = httpServletRequest.getHeader("Authorization");
        final var id = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User id %s not found", id)));
    }
}
