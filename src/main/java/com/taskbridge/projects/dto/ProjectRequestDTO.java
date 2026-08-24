package com.taskbridge.projects.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for Project creation requests.
 */
public record ProjectRequestDTO(
		@NotBlank String name,
		String description,
		@NotBlank String status,
		@NotNull Long teamId
) {}