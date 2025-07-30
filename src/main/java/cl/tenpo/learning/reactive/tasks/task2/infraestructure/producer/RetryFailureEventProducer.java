package cl.tenpo.learning.reactive.tasks.task2.infraestructure.producer;

import cl.tenpo.learning.reactive.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryFailureEventProducer {

    private static final String TOPIC = "CR_RETRY_EXHAUSTED";

    private final ReactiveKafkaProducerTemplate<String, String> kafkaProducer;

    public Mono<Void> sendEvent(String errorMessage) {
        String jsonPayload = JsonUtil.escapeJson(String.format("{\"error\": \"%s\"}", errorMessage));
        return kafkaProducer.send(new ProducerRecord<>(TOPIC, jsonPayload))
                .doOnNext(result -> log.info("Event sent successfully: {}", jsonPayload))
                .doOnError(e -> log.error("Failed to send event: {}", e.getMessage()))
                .then();
    }
}
