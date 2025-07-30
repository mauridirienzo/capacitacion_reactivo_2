package cl.tenpo.learning.reactive.tasks.task2.infraestructure.router.function;

import cl.tenpo.learning.reactive.tasks.task2.infraestructure.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class UserFunction {

    private final UserHandler userHandler;

    public RouterFunction<ServerResponse> configuration() {
        return RouterFunctions.route()
                .POST("users", userHandler::addUser)
                .DELETE("users/{username}", userHandler::removeUser)
                .build();
    }
}