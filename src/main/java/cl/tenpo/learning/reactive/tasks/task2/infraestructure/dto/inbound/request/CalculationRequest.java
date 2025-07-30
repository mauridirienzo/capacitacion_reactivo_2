package cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.request;

import lombok.Builder;
import jakarta.validation.constraints.NotNull;

@Builder
public record CalculationRequest(@NotNull(message = "number1 is required") Double number1,
                                 @NotNull(message = "number1 is required") Double number2) {}
