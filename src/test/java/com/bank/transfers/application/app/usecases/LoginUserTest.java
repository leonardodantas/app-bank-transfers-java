package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.UserNotFoundException;
import com.bank.transfers.application.app.security.IGenerateToken;
import com.bank.transfers.application.app.usecases.impl.LoginUser;
import com.bank.transfers.application.infra.http.requests.UserLoginRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginUserTest {

    @InjectMocks
    public LoginUser loginUser;
    @Mock
    private IGenerateToken generateToken;

    @Test
    public void shouldGenerateToken() {
        final var request = new UserLoginRequest("user@email.com", "123456");

        when(generateToken.execute(request.email(), request.password()))
                .thenReturn("55808e43-8d18-46ad-9dd5-899bf5d41274");

        final var result = loginUser.execute(request.email(), request.password());

        assertThat(result).isNotEmpty();
    }

    @Test(expected = UserNotFoundException.class)
    public void emailNotFound() {
        final var request = new UserLoginRequest("user@email.com", "123456");

        when(generateToken.execute(request.email(), request.password()))
                .thenThrow(UserNotFoundException.class);

        loginUser.execute(request.email(), request.password());
    }


    @Test(expected = UserNotFoundException.class)
    public void incorrectPassword() {
        final var request = new UserLoginRequest("user@email.com", "123456");

        when(generateToken.execute(request.email(), request.password()))
                .thenThrow(UserNotFoundException.class);

        loginUser.execute(request.email(), request.password());
    }

}
