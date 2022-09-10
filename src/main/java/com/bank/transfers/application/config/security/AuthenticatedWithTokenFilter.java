package com.bank.transfers.application.config.security;

import com.bank.transfers.application.app.exceptions.UserNotFoundException;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticatedWithTokenFilter  {

    private final IUserRepository userRepository;
    @Value("${jwt.secret}")
    private String secret;

    public AuthenticatedWithTokenFilter(final IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        final var token = this.getToken(request);
        final var valid = this.isTokenValid(token);
        if (valid) {
            authenticateClient(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateClient(final String token) {
        final var userId = this.getUserId(token);
        final var user = userRepository.findById(userId).map(AuthenticatedUser::from)
                .orElseThrow(() -> new UserNotFoundException(String.format("UserId %s not found", userId)));
        final var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getToken(final HttpServletRequest request) {
        final var token = request.getHeader("Authorization");
        if (Strings.isNullOrEmpty(token) || token.isEmpty()) {
            return null;
        }
        return token;
    }

    private boolean isTokenValid(final String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getUserId(final String token) {
        final var claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return String.valueOf(claims.getSubject());
    }
}
