package cl.tenpo.learning.reactive.tasks.task2.domain.service;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.History;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HistoryService {
    Mono<Void> addHistory(String endpoint, Object request, Object responseOrError);

    Flux<History> getHistory(String username);
}
