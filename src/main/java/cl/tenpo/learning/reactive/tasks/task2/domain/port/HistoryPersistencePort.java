package cl.tenpo.learning.reactive.tasks.task2.domain.port;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.History;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HistoryPersistencePort {
    Mono<History> save(History history);
    Flux<History> findAll();

}
