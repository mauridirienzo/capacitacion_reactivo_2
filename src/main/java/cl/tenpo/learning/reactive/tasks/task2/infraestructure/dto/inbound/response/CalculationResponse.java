package cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.response;

import lombok.Builder;

@Builder
public record CalculationResponse(Double result) {}
