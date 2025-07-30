package cl.tenpo.learning.reactive.tasks.task2.infraestructure.client;

import cl.tenpo.learning.reactive.tasks.task2.domain.port.PercentagePort;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.response.PercentageResponse;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.producer.RetryFailureEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.naming.ServiceUnavailableException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class PercentageClient implements PercentagePort {
    private final PercentageClientProperties properties;
    private static final String KEY = "percentage";
    private static final Duration TTL = Duration.ofMinutes(30);

    private final RetryFailureEventProducer producer;
    private final WebClient webClient;
    private final ReactiveStringRedisTemplate redisTemplate;

    public Mono<Double> getPercentage() {
        return getPercentageExternal()
                .doOnNext(percentage -> log.info("Fetched percentage from external API: {}", percentage))
                .flatMap(this::savePercentage)
                .doOnError(error -> log.error("Error fetching percentage from external API: {}",
                        error.getMessage(), error))
                .doOnError(error -> {
                    producer.sendEvent(error.getMessage()).subscribe();
                })
                .onErrorResume(error -> handleExternalPercentageError());
    }

    private Mono<Double> handleExternalPercentageError() {
        return  getCachedPercentage()
                .doOnNext(val -> log.info("Using cached percentage: {}", val))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new ServiceUnavailableException("Failed to fetch percentage"))));
    }

    private Mono<Double> getPercentageExternal() {
        return webClient.get()
                .uri(properties.getUrl())
                .retrieve()
                .bodyToMono(PercentageResponse.class)
                .map(PercentageResponse::percentage)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .onRetryExhaustedThrow((spec, signal) -> signal.failure()));
    }

    private Mono<Double> savePercentage(Double value) {
        return redisTemplate.opsForValue()
                .set(KEY, value.toString(), TTL)
                .thenReturn(value);
    }

    private Mono<Double> getCachedPercentage() {
        return redisTemplate.opsForValue()
                .get(KEY)
                .map(Double::parseDouble);
    }
}
