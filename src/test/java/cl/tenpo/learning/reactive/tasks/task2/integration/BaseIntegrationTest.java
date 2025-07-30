package cl.tenpo.learning.reactive.tasks.task2.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class BaseIntegrationTest {

    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15").withDatabaseName("learning_reactive_r2dbc")
                    .withUsername("postgresql")
                    .withPassword("postgresql");

    static final MongoDBContainer mongo =
            new MongoDBContainer("mongo:6.0");

    static final GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:7.0")).withExposedPorts(6379);

    static final KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @BeforeAll
    static void startContainers() {
        postgres.start();
        mongo.start();
        redis.start();
        kafka.start();
    }

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        // PostgreSQL
        registry.add("spring.r2dbc.url", () ->
                String.format("r2dbc:postgresql://%s:%d/%s", postgres.getHost(),
                        postgres.getMappedPort(5432), postgres.getDatabaseName()));
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);

        // MongoDB
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);

        // Redis
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));

        // Kafka
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
}
