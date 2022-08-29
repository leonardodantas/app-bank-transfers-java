package com.bank.transfers.application.config.security;

import com.bank.transfers.application.app.repositories.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    public MyUserDetailsService(final IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s not found", username)));

        return AuthenticatedUser.from(user);
    }
}
