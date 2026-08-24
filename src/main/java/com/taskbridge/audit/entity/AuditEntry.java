package com.taskbridge.audit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing an immutable audit entry for the TaskBridge Notification & Audit Service.
 * This entity is used to store audit records for project milestone events.
 */
@Entity
@Table(name = "audit_entries")
@Getter
@NoArgsConstructor
@ToString
public class AuditEntry {

    /**
     * Primary key for the audit entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The type of event (e.g., CREATED, UPDATED, CLOSED).
     */
    @NotBlank
    @Column(name = "event_type", nullable = false)
    private String eventType;

    /**
     * The type of entity being audited (e.g., ProjectMilestone).
     */
    @NotBlank
    @Column(name = "entity_type", nullable = false)
    private String entityType;

    /**
     * The unique identifier of the entity.
     */
    @NotNull
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    /**
     * The user ID of the actor who triggered the event.
     */
    @NotBlank
    @Column(name = "actor_user_id", nullable = false)
    private String actorUserId;

    /**
     * The ID of the organization to which the entity belongs.
     */
    @NotBlank
    @Column(name = "organization_id", nullable = false)
    private String organizationId;

    /**
     * A JSON string representing the state of the entity before the event.
     */
    @Column(name = "previous_state_snapshot", columnDefinition = "TEXT")
    private String previousStateSnapshot;

    /**
     * A JSON string representing the state of the entity after the event.
     */
    @NotBlank
    @Column(name = "new_state_snapshot", nullable = false, columnDefinition = "TEXT")
    private String newStateSnapshot;

    /**
     * The timestamp when the event occurred.
     */
    @NotNull
    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime timestamp;

    /**
     * The timestamp when the audit entry was created.
     * Automatically populated by the database.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Constructor for creating an immutable AuditEntry.
     *
     * @param eventType            The type of event.
     * @param entityType           The type of entity being audited.
     * @param entityId             The unique identifier of the entity.
     * @param actorUserId          The user ID of the actor who triggered the event.
     * @param organizationId       The ID of the organization to which the entity belongs.
     * @param previousStateSnapshot The state of the entity before the event.
     * @param newStateSnapshot     The state of the entity after the event.
     * @param timestamp            The timestamp when the event occurred.
     */
    public AuditEntry(String eventType, String entityType, Long entityId, String actorUserId, String organizationId,
                      String previousStateSnapshot, String newStateSnapshot, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.actorUserId = actorUserId;
        this.organizationId = organizationId;
        this.previousStateSnapshot = previousStateSnapshot;
        this.newStateSnapshot = newStateSnapshot;
        this.timestamp = timestamp;
    }
}