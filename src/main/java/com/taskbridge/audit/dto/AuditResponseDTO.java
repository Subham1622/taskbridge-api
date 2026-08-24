package com.taskbridge.audit.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for responding with immutable audit record details.
 * This DTO is used to transfer data for an AuditEntry in the TaskBridge Notification & Audit Service.
 *
 * @param id                   The unique identifier of the audit record.
 * @param eventType            The type of event (e.g., CREATED, UPDATED, CLOSED).
 * @param entityType           The type of entity being audited (e.g., ProjectMilestone).
 * @param entityId             The unique identifier of the entity.
 * @param actorUserId          The user ID of the actor who triggered the event.
 * @param organizationId       The ID of the organization to which the entity belongs.
 * @param previousStateSnapshot A JSON string representing the state of the entity before the event (nullable for CREATE events).
 * @param newStateSnapshot     A JSON string representing the state of the entity after the event.
 * @param timestamp            The timestamp when the event occurred.
 */
public record AuditResponseDTO(
        Long id,
        String eventType,
        String entityType,
        Long entityId,
        String actorUserId,
        String organizationId,
        String previousStateSnapshot,
        String newStateSnapshot,
        LocalDateTime timestamp
) {
}