package cl.tenpo.learning.reactive.tasks.task2.infraestructure.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external-percentage")
public class PercentageClientProperties {
    private String url;
}