package cl.tenpo.learning.reactive.tasks.task2.infraestructure.router.function;

import cl.tenpo.learning.reactive.tasks.task2.infraestructure.handler.CalculationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class CalculationFunction {

  private final CalculationHandler calculationHandler;

  public RouterFunction<ServerResponse> configuration() {
    return RouterFunctions.route()
            .POST("calculation", calculationHandler::calculate)
            .build();
  }
}
