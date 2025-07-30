package cl.tenpo.learning.reactive.tasks.task2.domain.port;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.User;
import reactor.core.publisher.Mono;

public interface AuthorizationPersistencePort {
    Mono<User> save(User user);
    Mono<Void> delete(User user);
    Mono<Boolean> findByUsername(User user);

}
