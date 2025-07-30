package cl.tenpo.learning.reactive.tasks.task2.infraestructure.router;

import cl.tenpo.learning.reactive.tasks.task2.infraestructure.api.error.ExceptionHandler;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.router.function.CalculationFunction;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.router.function.HistoryFunction;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.router.function.UserFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@RequiredArgsConstructor
@Configuration
public class MainRouterConfig {

    private final CalculationFunction calculationFunction;
    private final HistoryFunction historyFunction;
    private final UserFunction userFunction;
    private final ExceptionHandler exceptionHandler;


    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route()
                .path(buildBasePath(), calculationFunction::configuration)
                .path(buildBasePath(), userFunction::configuration)
                .path(buildBasePath(), historyFunction::configuration)
                .onError(Throwable.class, exceptionHandler::handle)
                .build();
    }

    private String buildBasePath() {
        return "/";
    }
}