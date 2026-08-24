package com.taskbridge.notifications.controller;

import com.taskbridge.notifications.dto.NotificationResponseDTO;
import com.taskbridge.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing notifications in the TaskBridge Notification & Audit Service.
 * <p>
 * This controller enforces multi-tenant isolation and provides endpoints for retrieving
 * and updating notifications within the context of a specific tenant.
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Retrieves notifications for a specific user within the tenant context.
     *
     * @param userId     The ID of the user.
     * @param tenantId   The tenant ID from the X-Tenant-ID header.
     * @return A list of notifications as DTOs.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByUser(
            @PathVariable String userId,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("Retrieving notifications for userId={} in tenantId={}", userId, tenantId);
        List<NotificationResponseDTO> notifications = notificationService.getNotificationsByRecipient(userId, tenantId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Retrieves unread notifications for a specific user within the tenant context.
     *
     * @param userId     The ID of the user.
     * @param tenantId   The tenant ID from the X-Tenant-ID header.
     * @return A list of unread notifications as DTOs.
     */
    @GetMapping("/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotificationsByUser(
            @PathVariable String userId,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("Retrieving unread notifications for userId={} in tenantId={}", userId, tenantId);
        List<NotificationResponseDTO> unreadNotifications = notificationService.getUnreadNotifications(userId, tenantId);
        return ResponseEntity.ok(unreadNotifications);
    }

    /**
     * Retrieves notifications for a specific project within the tenant context.
     *
     * @param projectId  The ID of the project.
     * @param tenantId   The tenant ID from the X-Tenant-ID header.
     * @return A list of notifications as DTOs.
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByProject(
            @PathVariable Long projectId,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("Retrieving notifications for projectId={} in tenantId={}", projectId, tenantId);
        List<NotificationResponseDTO> notifications = notificationService.getNotificationsByProject(projectId, tenantId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Retrieves notifications for a specific event type within the tenant context.
     *
     * @param eventType  The type of the event.
     * @param tenantId   The tenant ID from the X-Tenant-ID header.
     * @return A list of notifications as DTOs.
     */
    @GetMapping("/event/{eventType}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByEventType(
            @PathVariable String eventType,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("Retrieving notifications for eventType={} in tenantId={}", eventType, tenantId);
        List<NotificationResponseDTO> notifications = notificationService.getNotificationsByEventType(eventType, tenantId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Marks a notification as read within the tenant context.
     *
     * @param notificationId The ID of the notification.
     * @param tenantId       The tenant ID from the X-Tenant-ID header.
     * @return A ResponseEntity with no content.
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markNotificationAsRead(
            @PathVariable("id") Long notificationId,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("Marking notification with id={} as read in tenantId={}", notificationId, tenantId);
        notificationService.markNotificationAsRead(notificationId, tenantId);
        return ResponseEntity.noContent().build();
    }
}