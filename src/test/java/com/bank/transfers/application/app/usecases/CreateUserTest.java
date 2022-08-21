package com.bank.transfers.application.app.usecases;

import com.bank.transfers.application.app.exceptions.AlreadyUserException;
import com.bank.transfers.application.app.repositories.IUserRepository;
import com.bank.transfers.application.app.usecases.impl.CreateUser;
import com.bank.transfers.application.http.converters.UserConverter;
import com.bank.transfers.application.http.requests.UserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateUserTest {

    @InjectMocks
    private CreateUser createUser;

    @Mock
    private IUserRepository userRepository;

    @Test
    public void createUser() {
        final var request = new UserRequest("Leonardo Dantas", "312.778.123-09", "user@email.com", "123456");

        when(userRepository.save(any()))
                .thenReturn(UserConverter.toDomain(request));

        final var result = createUser.execute(UserConverter.toDomain(request));

        assertThat(result).isNotNull();
        verify(userRepository, times(1)).save(any());
    }

    @Test(expected = AlreadyUserException.class)
    public void alreadyExistUserWithEmail() {
        final var request = new UserRequest("Leonardo Dantas", "312.778.123-09", "user@email.com", "123456");

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(UserConverter.toDomain(request)));

        createUser.execute(UserConverter.toDomain(request));
    }

    @Test(expected = AlreadyUserException.class)
    public void alreadyExistUserWithDocumentation() {
        final var request = new UserRequest("Leonardo Dantas", "312.778.123-09", "user@email.com", "123456");

        when(userRepository.findByDocument(anyString()))
                .thenReturn(Optional.of(UserConverter.toDomain(request)));

        createUser.execute(UserConverter.toDomain(request));
    }
}
