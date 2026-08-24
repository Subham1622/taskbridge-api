package com.taskbridge.audit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for creating an immutable audit record.
 * This DTO is used to transfer data for creating an AuditEntry in the TaskBridge Notification & Audit Service.
 *
 * @param eventType            The type of event (e.g., CREATED, UPDATED, CLOSED).
 * @param entityType           The type of entity being audited (e.g., ProjectMilestone).
 * @param entityId             The unique identifier of the entity.
 * @param actorUserId          The user ID of the actor who triggered the event.
 * @param organizationId       The ID of the organization to which the entity belongs.
 * @param previousStateSnapshot A JSON string representing the state of the entity before the event.
 * @param newStateSnapshot     A JSON string representing the state of the entity after the event.
 */
public record AuditRequestDTO(
        @NotBlank(message = "Event type must not be blank")
        String eventType,

        @NotBlank(message = "Entity type must not be blank")
        String entityType,

        @NotNull(message = "Entity ID must not be null")
        Long entityId,

        @NotBlank(message = "Actor user ID must not be blank")
        String actorUserId,

        @NotBlank(message = "Organization ID must not be blank")
        String organizationId,
                
        String previousStateSnapshot,

        @NotBlank(message = "New state snapshot must not be blank")
        String newStateSnapshot
) {
}