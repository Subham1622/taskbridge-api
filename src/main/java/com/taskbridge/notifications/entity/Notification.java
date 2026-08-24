package com.taskbridge.notifications.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a Notification in the TaskBridge Notification & Audit Service.
 * <p>
 * Notifications are generated for project milestones (CREATED, UPDATED, CLOSED)
 * and are scoped to a specific organization in a multi-tenant SaaS environment.
 * </p>
 */
@Entity
@Getter
@ToString
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "recipient_user_id", nullable = false)
    private String recipientUserId;

    @NotNull
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "event_type", nullable = false)
    private String eventType;

    @NotBlank
    @Size(max = 500)
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "read_status", nullable = false)
    private Boolean readStatus = false;

    @CreationTimestamp
    @Column(name = "created_timestamp", nullable = false, updatable = false)
    private LocalDateTime createdTimestamp;

    @NotBlank
    @Column(name = "organization_id", nullable = false)
    private String organizationId;

    /**
     * Default constructor for JPA.
     */
    protected Notification() {
        // Default constructor for JPA
    }

    /**
     * Constructs a new Notification.
     *
     * @param recipientUserId The ID of the user receiving the notification.
     * @param projectId       The ID of the project associated with the notification.
     * @param eventType       The type of event triggering the notification.
     * @param message         The message content of the notification.
     * @param organizationId  The organization ID for multi-tenant isolation.
     */
    public Notification(String recipientUserId, Long projectId, String eventType, String message, String organizationId) {
        this.recipientUserId = recipientUserId;
        this.projectId = projectId;
        this.eventType = eventType;
        this.message = message;
        this.organizationId = organizationId;
        this.readStatus = false; // Default to unread
    }
    
    /**
     * Marks the notification as read.
     */
    public void markAsRead() {
        this.readStatus = true;
    }
}