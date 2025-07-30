package cl.tenpo.learning.reactive.tasks.task2.domain.service;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.Calculation;
import reactor.core.publisher.Mono;

public interface CalculationService {
    Mono<Double> calculate(Calculation calculation);
}