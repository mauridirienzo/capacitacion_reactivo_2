package cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.request;

import lombok.Builder;

@Builder
public record AddUserRequest(String username) {}
