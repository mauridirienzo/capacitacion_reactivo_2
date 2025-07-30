package cl.tenpo.learning.reactive.tasks.task2.infraestructure.handler;

import cl.tenpo.learning.reactive.tasks.task2.domain.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HistoryHandler {

    private final HistoryService historyService;

    public Mono<ServerResponse> getHistory(ServerRequest request) {
        return Mono.justOrEmpty(request.headers().firstHeader("username"))
                .switchIfEmpty(Mono.error(() -> new IllegalAccessException("User_name not found int header")))
                .flatMapMany(historyService::getHistory)
                .collectList()
                .flatMap(h -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(h));
    }
}