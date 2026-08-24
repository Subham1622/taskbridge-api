package com.taskbridge.notifications.service;

import com.taskbridge.notifications.dto.NotificationResponseDTO;
import com.taskbridge.notifications.entity.Notification;
import com.taskbridge.notifications.exception.NotificationNotFoundException;
import com.taskbridge.notifications.repository.NotificationRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing notifications in the TaskBridge Notification & Audit Service.
 * <p>
 * This service enforces multi-tenant isolation and provides methods for creating,
 * retrieving, and updating notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Creates a new notification.
     *
     * @param notification The notification entity to be created.
     * @return The created notification as a DTO.
     */
    @Transactional
    public NotificationResponseDTO createNotification(Notification notification) {
        log.info("Creating notification for recipientUserId={} in organizationId={}",
                notification.getRecipientUserId(), notification.getOrganizationId());
        Notification savedNotification = notificationRepository.save(notification);
        return mapToDTO(savedNotification);
    }

    /**
     * Retrieves notifications by recipientUserId and organizationId.
     *
     * @param recipientUserId The ID of the recipient user.
     * @param organizationId  The ID of the organization.
     * @return A list of notifications as DTOs.
     */
    public List<NotificationResponseDTO> getNotificationsByRecipient(String recipientUserId, String organizationId) {
        log.info("Retrieving notifications for recipientUserId={} in organizationId={}", recipientUserId, organizationId);
        return notificationRepository.findByRecipientUserIdAndOrganizationId(recipientUserId, organizationId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves unread notifications by recipientUserId and organizationId.
     *
     * @param recipientUserId The ID of the recipient user.
     * @param organizationId  The ID of the organization.
     * @return A list of unread notifications as DTOs.
     */
    public List<NotificationResponseDTO> getUnreadNotifications(String recipientUserId, String organizationId) {
        log.info("Retrieving unread notifications for recipientUserId={} in organizationId={}", recipientUserId, organizationId);
        return notificationRepository.findUnreadByRecipientUserIdAndOrganizationId(recipientUserId, organizationId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves notifications by projectId and organizationId.
     *
     * @param projectId      The ID of the project.
     * @param organizationId The ID of the organization.
     * @return A list of notifications as DTOs.
     */
    public List<NotificationResponseDTO> getNotificationsByProject(Long projectId, String organizationId) {
        log.info("Retrieving notifications for projectId={} in organizationId={}", projectId, organizationId);
        return notificationRepository.findByProjectIdAndOrganizationId(projectId, organizationId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves notifications by eventType and organizationId.
     *
     * @param eventType      The type of the event.
     * @param organizationId The ID of the organization.
     * @return A list of notifications as DTOs.
     */
    public List<NotificationResponseDTO> getNotificationsByEventType(String eventType, String organizationId) {
        log.info("Retrieving notifications for eventType={} in organizationId={}", eventType, organizationId);
        return notificationRepository.findByEventTypeAndOrganizationId(eventType, organizationId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Marks a notification as read.
     *
     * @param notificationId The ID of the notification.
     * @param organizationId The ID of the organization.
     */
    @Transactional
    public void markNotificationAsRead(Long notificationId, String organizationId) {
        log.info("Marking notification with id={} as read in organizationId={}", notificationId, organizationId);
        Notification notification = notificationRepository.findByIdAndOrganizationId(notificationId, organizationId)
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification with id=" + notificationId + " not found in organizationId=" + organizationId));
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    /**
     * Maps a Notification entity to a NotificationResponseDTO.
     *
     * @param notification The notification entity.
     * @return The mapped NotificationResponseDTO.
     */
    private NotificationResponseDTO mapToDTO(Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getRecipientUserId(),
                notification.getProjectId(),
                notification.getEventType(),
                notification.getMessage(),
                notification.getReadStatus(),
                notification.getCreatedTimestamp(),
                notification.getOrganizationId()
        );
    }
}