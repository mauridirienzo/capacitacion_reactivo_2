package cl.tenpo.learning.reactive.tasks.task2.adapter;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.History;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.entity.HistoryRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HistoryAdapter {

  public History toModel(String endpoint, String request, String responseOrError){
    return History.builder()
            .endpoint(endpoint)
            .request(request)
            .response(responseOrError)
            .build();
  }

  public  HistoryRecord toEntity(History history){
    return HistoryRecord.builder()
        .response(history.response())
        .request(history.request())
        .endpoint(history.endpoint())
        .timestamp(LocalDateTime.now())
        .build();
  }
  public History toModel(HistoryRecord historyRecord) {
    return History.builder()
        .request(historyRecord.getRequest())
        .response(historyRecord.getResponse())
        .endpoint(historyRecord.getEndpoint())
        .date(historyRecord.getTimestamp().toLocalDate().toString())
        .time(historyRecord.getTimestamp().toLocalTime().toString())
        .build();
  }
}
