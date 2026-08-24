package com.taskbridge.projects.dto;

import java.time.LocalDateTime;

/**
 * DTO for Project responses.
 */
public record ProjectResponseDTO(
		Long id,
		String name,
		String description,
		String status,
		Long teamId,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
) {}