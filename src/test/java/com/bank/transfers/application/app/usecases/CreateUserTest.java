package com.bank.transfers.application.app.usecases;

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
    private CreateUserUsecase createUser;

    @Mock
    private IUserRepository userRepository;

    @Test
    public void createUser(){
        final UserRequest request = new UserRequest("Leonardo Dantas", "312.778.123-09", "user@email.com", "123456");
        final var result = createUser.execute(UserRequest.toDomain(request));

        assertThat(result).isNotNull();
        verify(userRepository, times(1)).save(any());
    }

    @Test(expected = AlreadyUserException.class)
    public void alreadyExistUserWithEmail(){

        final UserRequest request = new UserRequest("Leonardo Dantas", "312.778.123-09", "user@email.com", "123456");
        final User user = UserConverter.toDomain(request);

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        createUser.execute(UserConverter.toDomain(request));

    }


    @Test(expected = AlreadyUserException.class)
    public void alreadyExistUserWithDocumentation(){
        final UserRequest request = new UserRequest("Leonardo Dantas", "312.778.123-09", "user@email.com", "123456");

        final User user = UserConverter.toDomain(request);

        when(userRepository.findByDocument(anyString()))
                .thenReturn(Optional.of(user));

        final var result = createUser.execute(UserRequest.toDomain(request));

    }
}
