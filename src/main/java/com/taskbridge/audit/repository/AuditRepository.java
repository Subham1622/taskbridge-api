package com.taskbridge.audit.repository;

import com.taskbridge.audit.entity.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing AuditEntry entities in the TaskBridge Notification & Audit Service.
 * This repository enforces multi-tenant SaaS isolation by requiring organizationId in all queries.
 */
public interface AuditRepository extends JpaRepository<AuditEntry, Long> {

    /**
     * Retrieves the audit history for a specific entity within a given organization.
     *
     * @param entityId       The unique identifier of the entity.
     * @param organizationId The ID of the organization to which the entity belongs.
     * @return A list of AuditEntry records matching the entityId and organizationId.
     */
    List<AuditEntry> findByEntityIdAndOrganizationId(Long entityId, String organizationId);

    /**
     * Retrieves audit entries filtered by event type within a given organization.
     *
     * @param eventType      The type of event (e.g., CREATED, UPDATED, CLOSED).
     * @param organizationId The ID of the organization to which the entries belong.
     * @return A list of AuditEntry records matching the eventType and organizationId.
     */
    List<AuditEntry> findByEventTypeAndOrganizationId(String eventType, String organizationId);

    /**
     * Retrieves audit entries within a specific date range for a given organization.
     *
     * @param startDate      The start of the date range (inclusive).
     * @param endDate        The end of the date range (inclusive).
     * @param organizationId The ID of the organization to which the entries belong.
     * @return A list of AuditEntry records within the specified date range and organizationId.
     */
    @Query("SELECT a FROM AuditEntry a WHERE a.timestamp BETWEEN :startDate AND :endDate AND a.organizationId = :organizationId")
    List<AuditEntry> findByTimestampBetweenAndOrganizationId(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("organizationId") String organizationId
    );

    /**
     * Retrieves audit entries for a specific entity and event type within a given organization.
     *
     * @param entityId       The unique identifier of the entity.
     * @param eventType      The type of event (e.g., CREATED, UPDATED, CLOSED).
     * @param organizationId The ID of the organization to which the entries belong.
     * @return A list of AuditEntry records matching the entityId, eventType, and organizationId.
     */
    List<AuditEntry> findByEntityIdAndEventTypeAndOrganizationId(
        Long entityId,
        String eventType,
        String organizationId
    );
}