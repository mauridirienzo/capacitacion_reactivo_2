package cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "history")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryRecord {

    @Id
    private String id;
    private String endpoint;
    private String request;
    private String response;
    private LocalDateTime timestamp;
}
