package cl.tenpo.learning.reactive.tasks.task2.infraestructure.repository.impl;

import cl.tenpo.learning.reactive.tasks.task2.adapter.UserAdapter;
import cl.tenpo.learning.reactive.tasks.task2.domain.model.User;
import cl.tenpo.learning.reactive.tasks.task2.domain.port.AuthorizationPersistencePort;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.repository.AuthorizedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
  public class AuthorizedPersistenceImpl implements AuthorizationPersistencePort {

  private final AuthorizedRepository authorizedUserRepository;
  private final UserAdapter userAdapter;

  @Override
  public Mono<User> save(User user) {
    return Mono.just(user)
        .map(userAdapter::toEntity)
        .flatMap(authorizedUserRepository::save)
        .map(userAdapter::toModel);
  }

  @Override
  public Mono<Void> delete(User user) {
    return Mono.just(user)
            .map(userAdapter::toEntity)
            .flatMap(authorizedUserRepository::delete);
  }

  @Override
  public Mono<Boolean> findByUsername(User user) {
    return Mono.just(user)
        .flatMap(u -> authorizedUserRepository.findByUsername(u.username()))
        .hasElement();
  }
}
