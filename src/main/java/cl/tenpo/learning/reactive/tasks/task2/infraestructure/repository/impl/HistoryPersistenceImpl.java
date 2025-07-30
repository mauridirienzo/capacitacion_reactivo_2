package cl.tenpo.learning.reactive.tasks.task2.infraestructure.repository.impl;

import cl.tenpo.learning.reactive.tasks.task2.adapter.HistoryAdapter;
import cl.tenpo.learning.reactive.tasks.task2.domain.model.History;
import cl.tenpo.learning.reactive.tasks.task2.domain.port.HistoryPersistencePort;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class HistoryPersistenceImpl implements HistoryPersistencePort {

  private final HistoryRepository historyRepository;
  private final HistoryAdapter historyAdapter;

  @Override
  public Mono<History> save(History history) {
    return Mono.just(history)
        .map(historyAdapter::toEntity)
        .flatMap(historyRepository::save)
        .map(historyAdapter::toModel);
  }

  @Override
  public Flux<History> findAll() {
    return historyRepository.findAll()
        .map(historyAdapter::toModel);
  }
}
