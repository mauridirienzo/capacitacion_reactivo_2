package cl.tenpo.learning.reactive.tasks.task2.domain.service;

import cl.tenpo.learning.reactive.tasks.task2.adapter.UserAdapter;
import cl.tenpo.learning.reactive.tasks.task2.domain.model.User;
import cl.tenpo.learning.reactive.tasks.task2.domain.port.AuthorizationPersistencePort;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.impl.AuthorizationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceImplTest {
    @Mock
    private AuthorizationPersistencePort repository;
    @Mock
    private UserAdapter userAdapter;
    @InjectMocks
    private AuthorizationServiceImpl service;

    @Test
    @DisplayName("TEST addUser WHEN user is added THEN return user")
    void test1() {
        String username = "testuser";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .build();

        when(userAdapter.toModel(username)).thenReturn(user);
        when(repository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(service.addUser(username))
                .expectNext(user)
                .verifyComplete();

        verify(userAdapter).toModel(username);
        verify(repository).save(user);
    }

    @Test
    @DisplayName("TEST addUser WHEN saving user fails THEN return error")
    void test2() {
        String username = "testuser";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .build();
        when(userAdapter.toModel(username)).thenReturn(user);
        when(repository.save(user)).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(service.addUser(username))
                .expectErrorMessage("DB error")
                .verify();

        verify(userAdapter).toModel(username);
        verify(repository).save(user);
    }

    @Test
    @DisplayName("TEST removeUser WHEN user is removed THEN complete")
    void test3() {
        String username = "testuser";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .build();
        when(userAdapter.toModel(username)).thenReturn(user);
        when(repository.delete(user)).thenReturn(Mono.empty());

        StepVerifier.create(service.removeUser(username))
                .verifyComplete();

        verify(userAdapter).toModel(username);
        verify(repository).delete(user);
    }

    @Test
    @DisplayName("TEST removeUser WHEN removing user fails THEN return error")
    void test4() {
        String username = "testuser";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .build();
        when(userAdapter.toModel(username)).thenReturn(user);
        when(repository.delete(user)).thenReturn(Mono.error(new RuntimeException("Delete error")));

        StepVerifier.create(service.removeUser(username))
                .expectErrorMessage("Delete error")
                .verify();

        verify(userAdapter).toModel(username);
        verify(repository).delete(user);
    }

    @Test
    @DisplayName("TEST isAuthorized WHEN user is authorized THEN return true")
    void test5() {
        String username = "testuser";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .build();
        when(userAdapter.toModel(username)).thenReturn(user);
        when(repository.findByUsername(user)).thenReturn(Mono.just(true));

        StepVerifier.create(service.isAuthorized(username))
                .expectNext(true)
                .verifyComplete();

        verify(userAdapter).toModel(username);
        verify(repository).findByUsername(user);
    }

    @Test
    @DisplayName("TEST isAuthorized WHEN user is not authorized THEN return false")
    void test6() {
        String username = "testuser";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .build();

        when(userAdapter.toModel(username)).thenReturn(user);
        when(repository.findByUsername(user)).thenReturn(Mono.error(new RuntimeException("Find error")));

        StepVerifier.create(service.isAuthorized(username))
                .expectErrorMessage("Find error")
                .verify();

        verify(userAdapter).toModel(username);
        verify(repository).findByUsername(user);
    }
}