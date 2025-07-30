package cl.tenpo.learning.reactive.tasks.task2.infraestructure.repository;

import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.entity.HistoryRecord;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends ReactiveMongoRepository<HistoryRecord, String> {
}
