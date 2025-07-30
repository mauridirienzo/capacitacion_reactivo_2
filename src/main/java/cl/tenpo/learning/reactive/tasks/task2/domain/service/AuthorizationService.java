package cl.tenpo.learning.reactive.tasks.task2.domain.service;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.User;
import reactor.core.publisher.Mono;

public interface AuthorizationService {
    Mono<User> addUser(String username);
    Mono<Void> removeUser(String username);
    Mono<Boolean> isAuthorized(String username);
}