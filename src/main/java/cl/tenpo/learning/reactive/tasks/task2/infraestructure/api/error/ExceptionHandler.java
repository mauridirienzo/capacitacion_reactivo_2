package cl.tenpo.learning.reactive.tasks.task2.infraestructure.api.error;

import cl.tenpo.learning.reactive.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Slf4j
@Component
public class ExceptionHandler {

  private static final String DEFAULT_ERROR_MESSAGE = "No detail provided";

  private final Map<
          Class<? extends Throwable>,
          BiFunction<? extends Throwable, ServerRequest, Mono<ServerResponse>>>
      exceptionHandlers;

  public ExceptionHandler(final CustomExceptionHandler customExceptionHandler) {
    this.exceptionHandlers =
        Map.ofEntries(
            entry(
                IllegalArgumentException.class,
                customExceptionHandler::handleIllegalArgumentException),
            entry(
                    IllegalAccessException.class,
                customExceptionHandler::handleIllegalAccessException),
            entry(
                    Exception.class,
                customExceptionHandler::handleGenericException));
  }


  private <T extends Throwable>
      Map.Entry<Class<T>, BiFunction<T, ServerRequest, Mono<ServerResponse>>> entry(
          final Class<T> clazz, final BiFunction<T, ServerRequest, Mono<ServerResponse>> handler) {
    return Map.entry(clazz, (err, req) -> handler.apply(clazz.cast(err), req));
  }

  @SuppressWarnings("unchecked")
  public <T extends Throwable> Mono<ServerResponse> handle(
      final T err, final ServerRequest request) {
    Class<?> clazz = err.getClass();
    while (clazz != null) {
      BiFunction<T, ServerRequest, Mono<ServerResponse>> handler =
          (BiFunction<T, ServerRequest, Mono<ServerResponse>>) exceptionHandlers.get(clazz);
      if (handler != null) {
        return handler.apply(err, request);
      }
      clazz = clazz.getSuperclass();
    }
    return handleUnknownException(err, request);
  }

  private Mono<ServerResponse> handleUnknownException(
      final Throwable err, final ServerRequest request) {
    String message = Optional.ofNullable(err.getMessage()).orElse(DEFAULT_ERROR_MESSAGE);
    return request
        .bodyToFlux(Map.class)
        .collectList()
        .doOnNext(
            body ->
                log.error(
                    "Encountered unknown error - error: [{}] - request_body: {}",
                    message,
                    body,
                    err))
        .flatMap(
            body ->
                ExceptionUtil.buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR, "unknown_error", message));
  }
}
