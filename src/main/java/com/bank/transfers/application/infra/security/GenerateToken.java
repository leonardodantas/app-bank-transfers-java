package com.bank.transfers.application.infra.security;

import com.bank.transfers.application.app.security.IGenerateToken;
import com.bank.transfers.application.domains.User;
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
    public String execute(final String email, final String password) {
        final var usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        final var authentication = authManager.authenticate(usernamePassword);
        return getToken(authentication);
    }


    public String getToken(final Authentication authentication) {
        final var logged = (User) authentication.getPrincipal();
        final var today = new Date();
        final var expirationDate = new Date(today.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API User authorization")
                .claim("password", password)
                .setSubject(logged.id())
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
