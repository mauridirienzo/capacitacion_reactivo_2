package cl.tenpo.learning.reactive.tasks.task2.domain.model;

import lombok.Builder;

@Builder
public record History(
        String endpoint,
        String request,
        String response,
        String date,
        String time
) {}
