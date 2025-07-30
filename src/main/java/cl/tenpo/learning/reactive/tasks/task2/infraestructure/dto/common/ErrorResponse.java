package cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.common;

import java.util.List;

public record ErrorResponse(String code, List<String> errors) {}
