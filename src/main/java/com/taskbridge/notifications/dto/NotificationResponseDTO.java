package com.taskbridge.notifications.dto;

import java.time.LocalDateTime;

/**
 * Immutable DTO representing a Notification response in the TaskBridge Notification & Audit Service.
 * <p>
 * This DTO is used to transfer notification data to clients while ensuring immutability.
 * It matches the Notification entity exactly and includes all fields for multi-tenant isolation
 * and organization-level tracing.
 * </p>
 *
 * @param id              The unique identifier of the notification.
 * @param recipientUserId The ID of the user receiving the notification.
 * @param projectId       The ID of the project associated with the notification.
 * @param eventType       The type of event triggering the notification.
 * @param message         The message content of the notification.
 * @param readStatus      The read status of the notification.
 * @param createdTimestamp The timestamp when the notification was created.
 * @param organizationId  The organization ID for multi-tenant isolation and debugging.
 */
public record NotificationResponseDTO(
        Long id,
        String recipientUserId,
        Long projectId,
        String eventType,
        String message,
        Boolean readStatus,
        LocalDateTime createdTimestamp,
        String organizationId
) {
}