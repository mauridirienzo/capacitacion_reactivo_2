package cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("authorized_users")
@Builder
public record AuthorizedUser(@Id UUID id, String username) {}
