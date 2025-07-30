package cl.tenpo.learning.reactive.tasks.task2.domain.service.impl;

import cl.tenpo.learning.reactive.tasks.task2.adapter.UserAdapter;
import cl.tenpo.learning.reactive.tasks.task2.domain.model.User;
import cl.tenpo.learning.reactive.tasks.task2.domain.port.AuthorizationPersistencePort;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

  private final AuthorizationPersistencePort repository;
  private final UserAdapter userAdapter;

  @Override
  public Mono<User> addUser(String username) {
    return Mono.just(username)
            .map(userAdapter::toModel)
            .doOnNext(u -> log.info("Adding user: {}", u))
            .flatMap(repository::save)
            .doOnNext(u -> log.info("User added: {}", u))
            .doOnError(err -> log.error("Error adding user : {}, error: {}", username, err.getMessage()));
  }

  @Override
  public Mono<Void> removeUser(String username) {
    return Mono.just(username)
            .map(userAdapter::toModel)
            .doOnNext(u -> log.info("Removing user: {}", u))
            .flatMap(repository::delete)
            .doOnNext(u -> log.info("User removed: {}", u))
            .doOnError(err -> log.error("Error removing user : {}, error: {}", username, err.getMessage()));
  }

  @Override
  public Mono<Boolean> isAuthorized(String username) {
    return Mono.just(username)
            .map(userAdapter::toModel)
            .doOnNext(u -> log.info("Finding user: {}", u))
            .flatMap(repository::findByUsername)
            .doOnNext(u -> log.info("User found: {}", u))
            .doOnError(err -> log.error("Error finding user : {}, error: {}", username, err.getMessage()));
  }
}
