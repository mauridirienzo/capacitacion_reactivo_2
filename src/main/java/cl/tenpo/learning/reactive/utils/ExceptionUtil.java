package cl.tenpo.learning.reactive.utils;

import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public final class ExceptionUtil {

  private ExceptionUtil() {}

  public static Mono<ServerResponse> buildErrorResponse(
      final HttpStatus httpStatus, final String code, final String message) {
    return buildErrorResponse(httpStatus, code, Collections.singletonList(message));
  }

  public static Mono<ServerResponse> buildErrorResponse(
      final HttpStatus httpStatus, final String code, final List<String> errors) {
    return ServerResponse.status(httpStatus)
        .body(BodyInserters.fromValue(new ErrorResponse(code, errors)));
  }
}
