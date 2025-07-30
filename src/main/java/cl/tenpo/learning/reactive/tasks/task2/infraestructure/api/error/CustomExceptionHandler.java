package cl.tenpo.learning.reactive.tasks.task2.infraestructure.api.error;

import cl.tenpo.learning.reactive.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CustomExceptionHandler {

    public Mono<ServerResponse> handleIllegalArgumentException(
          final IllegalArgumentException err, final ServerRequest serverRequest) {
    return ExceptionUtil.buildErrorResponse(
            HttpStatus.BAD_REQUEST, "invalid_request", err.getMessage());
  }

  public Mono<ServerResponse> handleIllegalAccessException(
          final IllegalAccessException err, final ServerRequest serverRequest) {
    return ExceptionUtil.buildErrorResponse(
            HttpStatus.UNAUTHORIZED, "unauthorized_access" , err.getMessage());
  }

  public Mono<ServerResponse> handleGenericException(
          final Exception err, final ServerRequest serverRequest) {
      return ExceptionUtil.buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, "server_error" , err.getMessage());
  }
}
