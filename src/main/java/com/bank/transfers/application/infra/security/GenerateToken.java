package com.bank.transfers.application.infra.security;

import com.bank.transfers.application.app.security.IGenerateToken;
import com.bank.transfers.application.config.security.AuthenticatedUser;
import com.bank.transfers.application.domains.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GenerateToken implements IGenerateToken {

    private final AuthenticationManager authManager;
    private final String expiration;
    private final String secret;
    private final String password;

    public GenerateToken(
            final AuthenticationManager authManager,
            @Value("${jwt.expiration}") final String expiration,
            @Value("${jwt.secret}") final String secret,
            @Value("${jwt.password}") final String password) {
        this.authManager = authManager;
        this.expiration = expiration;
        this.secret = secret;
        this.password = password;
    }

    @Override
    public Token execute(final String email, final String password) {
        final var usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        final var authentication = authManager.authenticate(usernamePassword);
        return getToken(authentication);
    }

    private Token getToken(final Authentication authentication) {
        final var logged = (AuthenticatedUser) authentication.getPrincipal();
        final var createAt = new Date();
        final var expirationAt = new Date(createAt.getTime() + Long.parseLong(expiration));

        final var token = Jwts.builder()
                .setIssuer("API User authorization")
                .claim("password", password)
                .setSubject(logged.getId())
                .setIssuedAt(createAt)
                .setExpiration(expirationAt)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return Token.of(token, createAt, expirationAt);
    }
}
