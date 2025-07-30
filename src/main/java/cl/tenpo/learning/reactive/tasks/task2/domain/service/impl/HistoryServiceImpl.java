package cl.tenpo.learning.reactive.tasks.task2.domain.service.impl;

import cl.tenpo.learning.reactive.tasks.task2.adapter.HistoryAdapter;
import cl.tenpo.learning.reactive.tasks.task2.domain.model.History;
import cl.tenpo.learning.reactive.tasks.task2.domain.port.HistoryPersistencePort;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.AuthorizationService;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryPersistencePort historyPersistencePort;
    private final AuthorizationService authorizationService;
    private final HistoryAdapter historyAdapter;

    @Override
    public Mono<Void> addHistory(final String endpoint,
                                 final Object request,
                                 final Object responseOrError) {

        return Mono.just(request)
                .map(r -> historyAdapter.toModel(endpoint, r.toString(), responseOrError.toString()))
                .flatMap(historyPersistencePort::save)
                .doOnSuccess(r -> log.info("History saved: {}", r))
                .doOnError(e -> log.error("Error saving history", e))
                .onErrorResume(e -> Mono.empty())
                .then();
    }

    @Override
    public Flux<History> getHistory(final String username) {
        return Mono.just(username)
                        .doOnNext(u -> log.info("Checking authorization for user: {}", u))
                                .flatMap(authorizationService::isAuthorized)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new IllegalAccessException("User is not authorized")))
                .flatMapMany(ignored -> historyPersistencePort.findAll())
                .doOnNext(h -> log.info("Retrieved history: {}", h));
    }
}
