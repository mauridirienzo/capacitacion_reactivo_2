package cl.tenpo.learning.reactive.tasks.task2.infraestructure.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RetryFailureConsumer {
    @KafkaListener(topics = "CR_RETRY_EXHAUSTED", groupId = "retry-failure-group")
    public void consume(String message) {
        log.info("Received failed event from Kafka: {}", message);
    }
}
