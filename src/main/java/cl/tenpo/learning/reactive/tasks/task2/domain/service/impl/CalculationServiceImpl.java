package cl.tenpo.learning.reactive.tasks.task2.domain.service.impl;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.Calculation;
import cl.tenpo.learning.reactive.tasks.task2.domain.port.PercentagePort;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.CalculationService;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final PercentagePort percentagePort;
    private final HistoryService historyService;

    public Mono<Double> calculate(Calculation calculation) {
        return Mono.just(calculation.number1() + (calculation.number2()))
                .doOnNext(amount -> log.info("Calculated amount: {}", amount))
                .flatMap(this::applyPercentage)
                .flatMap(result ->
                        historyService.addHistory("/learning-reactive/calculation", calculation, result)
                                .thenReturn(result))
                .doOnError(error -> log.error("Error during calculation: {}", error.getMessage(), error))
                .onErrorResume(error ->
                        historyService.addHistory("/learning-reactive/calculation", calculation, error.getMessage())
                                .then(Mono.error(error)));
    }

    private Mono<Double> applyPercentage(Double amount) {
        return percentagePort.getPercentage()
                .doOnNext(percentage -> log.info("Retrieved percentage: {}", percentage))
                .map(percentage -> amount * (1 + percentage / 100))
                .map(result ->BigDecimal.valueOf(result).setScale(2, java.math.RoundingMode.HALF_UP).doubleValue())
                .doOnNext(result -> log.info("Final result after applying percentage: {}", result));
    }
}
