package cl.tenpo.learning.reactive.tasks.task2.domain.port;

import reactor.core.publisher.Mono;

public interface PercentagePort {
    Mono<Double> getPercentage();
}
