package cl.tenpo.learning.reactive.tasks.task2.domain.service;

import cl.tenpo.learning.reactive.tasks.task2.adapter.HistoryAdapter;
import cl.tenpo.learning.reactive.tasks.task2.domain.model.History;
import cl.tenpo.learning.reactive.tasks.task2.domain.port.HistoryPersistencePort;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.impl.HistoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceImplTest {

    @Mock
    private HistoryPersistencePort historyPersistencePort;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private HistoryAdapter historyAdapter;
    @InjectMocks
    private HistoryServiceImpl historyService;

    @Test
    @DisplayName("TEST addHistory WHEN request is ok THEN history is saved")
    void test1() {
        String endpoint = "/test";
        Object request = "req";
        Object response = "resp";

        History history = History.builder()
                .date(LocalDate.now().toString())
                .endpoint(endpoint)
                .request(request.toString())
                .response(response.toString())
                .build();

        when(historyAdapter.toModel(endpoint, request.toString(), response.toString())).thenReturn(history);
        when(historyPersistencePort.save(history)).thenReturn(Mono.just(history));

        StepVerifier.create(historyService.addHistory(endpoint, request, response))
                .verifyComplete();

        verify(historyPersistencePort).save(history);
    }

    @Test
    @DisplayName("TEST addHistory WHEN saving throws exception THEN return empty")
    void test2() {
        String username = "user";
        String endpoint = "/test";
        Object request = "req";
        Object response = "resp";

        History history = History.builder()
                .date(LocalDate.now().toString())
                .endpoint(endpoint)
                .request(request.toString())
                .response(response.toString())
                .build();

        when(authorizationService.isAuthorized(username)).thenReturn(Mono.just(true));

        when(historyPersistencePort.findAll()).thenReturn(Flux.just(history));

        StepVerifier.create(historyService.getHistory(username))
                .expectNext(history)
                .verifyComplete();
    }

    @Test
    @DisplayName("TEST getHistory WHEN user is authorized THEN return history")
    void test3() {
        String username = "user";
        when(authorizationService.isAuthorized(username)).thenReturn(Mono.just(false));

        StepVerifier.create(historyService.getHistory(username))
                .expectError(IllegalAccessException.class)
                .verify();
    }

    @Test
    @DisplayName("TEST getHistory WHEN user is unauthorized THEN throw IllegalAccessException")
    void test4() {
        String username = "unauthorizedUser";
        when(authorizationService.isAuthorized(username)).thenReturn(Mono.just(false));

        StepVerifier.create(historyService.getHistory(username))
                .expectError(IllegalAccessException.class)
                .verify();

        verify(authorizationService).isAuthorized(username);
        verifyNoInteractions(historyPersistencePort);
    }
}