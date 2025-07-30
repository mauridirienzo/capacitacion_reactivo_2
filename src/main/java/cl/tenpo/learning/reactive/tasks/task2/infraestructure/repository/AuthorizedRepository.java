package cl.tenpo.learning.reactive.tasks.task2.infraestructure.repository;

import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.entity.AuthorizedUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AuthorizedRepository extends ReactiveCrudRepository<AuthorizedUser, Long> {
    Mono<AuthorizedUser> findByUsername(String username);
}
