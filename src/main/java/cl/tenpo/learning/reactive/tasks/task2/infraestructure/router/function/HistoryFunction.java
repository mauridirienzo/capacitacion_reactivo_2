package cl.tenpo.learning.reactive.tasks.task2.infraestructure.router.function;

import cl.tenpo.learning.reactive.tasks.task2.infraestructure.handler.HistoryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class HistoryFunction {

    private final HistoryHandler historyHandler;

    public RouterFunction<ServerResponse> configuration() {
        return RouterFunctions.route()
                .GET("history", historyHandler::getHistory).build();
    }
}