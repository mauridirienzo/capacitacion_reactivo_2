package cl.tenpo.learning.reactive.tasks.task2.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record User(UUID id, String username) {}
