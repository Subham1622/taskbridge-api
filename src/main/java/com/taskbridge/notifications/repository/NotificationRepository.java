package com.taskbridge.notifications.repository;

import com.taskbridge.notifications.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Notification entities in the TaskBridge Notification & Audit Service.
 * <p>
 * This repository enforces multi-tenant isolation by requiring organizationId filtering
 * on all queries to prevent cross-tenant access.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Retrieves notifications by recipientUserId and organizationId.
     *
     * @param recipientUserId The ID of the recipient user.
     * @param organizationId  The ID of the organization.
     * @return A list of notifications for the specified recipient and organization.
     */
    List<Notification> findByRecipientUserIdAndOrganizationId(String recipientUserId, String organizationId);

    /**
     * Retrieves unread notifications by recipientUserId and organizationId.
     *
     * @param recipientUserId The ID of the recipient user.
     * @param organizationId  The ID of the organization.
     * @return A list of unread notifications for the specified recipient and organization.
     */
    @Query("SELECT n FROM Notification n WHERE n.recipientUserId = :recipientUserId AND n.organizationId = :organizationId AND n.readStatus = false")
    List<Notification> findUnreadByRecipientUserIdAndOrganizationId(@Param("recipientUserId") String recipientUserId, @Param("organizationId") String organizationId);

    /**
     * Retrieves notifications by projectId and organizationId.
     *
     * @param projectId      The ID of the project.
     * @param organizationId The ID of the organization.
     * @return A list of notifications for the specified project and organization.
     */
    List<Notification> findByProjectIdAndOrganizationId(Long projectId, String organizationId);

    /**
     * Retrieves notifications by eventType and organizationId.
     *
     * @param eventType      The type of the event.
     * @param organizationId The ID of the organization.
     * @return A list of notifications for the specified event type and organization.
     */
    List<Notification> findByEventTypeAndOrganizationId(String eventType, String organizationId);

    /**
     * Retrieves a notification by id and organizationId.
     *
     * @param id             The ID of the notification.
     * @param organizationId The ID of the organization.
     * @return An optional notification for the specified ID and organization.
     */
    Optional<Notification> findByIdAndOrganizationId(Long id, String organizationId);
}