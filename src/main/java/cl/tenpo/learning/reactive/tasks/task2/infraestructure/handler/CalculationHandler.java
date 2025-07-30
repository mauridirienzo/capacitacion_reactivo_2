package cl.tenpo.learning.reactive.tasks.task2.infraestructure.handler;

import cl.tenpo.learning.reactive.tasks.task2.adapter.CalculationAdapter;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.impl.CalculationServiceImpl;
import cl.tenpo.learning.reactive.tasks.task2.domain.service.HistoryService;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.request.CalculationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class CalculationHandler {

  private final CalculationServiceImpl calculationServiceImpl;
  private final HistoryService historyService;
  private final CalculationAdapter calculationAdapter;

  public Mono<ServerResponse> calculate(ServerRequest request) {
    return request.bodyToMono(CalculationRequest.class)
      .map(calculationAdapter::toModel)
      .flatMap(calculationServiceImpl::calculate)
      .map(calculationAdapter::toResponse)
      .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)))
      .doOnError(e -> log.error("Error in calculate handler error:{}", e.getMessage(), e));
  }
}
