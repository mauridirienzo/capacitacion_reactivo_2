package cl.tenpo.learning.reactive.tasks.task2.domain.service;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.Calculation;
import cl.tenpo.learning.reactive.tasks.task2.domain.port.PercentagePort;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.impl.CalculationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceImplTest {

    @Mock
    private PercentagePort percentagePort;
    @Mock
    private HistoryService historyService;
    @InjectMocks
    private CalculationServiceImpl calculationService;

    @Test
    @DisplayName("TEST calculate WHEN percentage is retrieved THEN return calculation result and log history")
    void test1() {
        Calculation calculation = new Calculation(10.0, 5.0);
        when(percentagePort.getPercentage()).thenReturn(Mono.just(20.0));
        when(historyService.addHistory(anyString(), any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(calculationService.calculate(calculation))
                .expectNext(18.0) // (10+5) * 1.2 = 18.0
                .verifyComplete();

        verify(historyService).addHistory(anyString(), eq(calculation), eq(18.0));
    }

    @Test
    @DisplayName("TEST calculate WHEN percentagePort fails THEN log error and return exception")
    void test2() {
        Calculation calculation = new Calculation(10.0, 5.0);
        RuntimeException error = new RuntimeException("Error");
        when(percentagePort.getPercentage()).thenReturn(Mono.error(error));
        when(historyService.addHistory(anyString(), any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(calculationService.calculate(calculation))
                .expectError(RuntimeException.class)
                .verify();

        verify(historyService).addHistory(anyString(), eq(calculation), eq("Error"));
    }
}