package com.taskbridge.audit.service;

import com.taskbridge.audit.dto.AuditRequestDTO;
import com.taskbridge.audit.dto.AuditResponseDTO;
import com.taskbridge.audit.entity.AuditEntry;
import com.taskbridge.audit.exception.AuditImmutableException;
import com.taskbridge.audit.repository.AuditRepository;

import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing audit records in the TaskBridge Notification & Audit Service.
 * This service enforces immutability and multi-tenant isolation for all audit operations.
 */
@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    private final AuditRepository auditRepository;

    /**
     * Constructor for AuditService.
     *
     * @param auditRepository The repository for managing AuditEntry entities.
     */
    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * Creates a new audit entry.
     *
     * @param requestDTO The DTO containing the details of the audit entry to be created.
     * @return The created audit entry as a response DTO.
     */
    @Transactional
    public AuditResponseDTO createAuditEntry(AuditRequestDTO requestDTO) {
        logger.info("Creating new audit entry for entityId: {}, organizationId: {}", requestDTO.entityId(), requestDTO.organizationId());

        AuditEntry auditEntry = new AuditEntry(
            requestDTO.eventType(),
            requestDTO.entityType(),
            requestDTO.entityId(),
            requestDTO.actorUserId(),
            requestDTO.organizationId(),
            requestDTO.previousStateSnapshot(),
            requestDTO.newStateSnapshot(),
            LocalDateTime.now()
        );    

        AuditEntry savedEntry = auditRepository.save(auditEntry);
        return mapToResponseDTO(savedEntry);
    }

    /**
     * Retrieves audit history by entityId and organizationId.
     *
     * @param entityId       The unique identifier of the entity.
     * @param organizationId The ID of the organization.
     * @return A list of audit entries as response DTOs.
     */
    public List<AuditResponseDTO> getAuditHistoryByEntityId(Long entityId, String organizationId) {
        logger.info("Retrieving audit history for entityId: {}, organizationId: {}", entityId, organizationId);
        return auditRepository.findByEntityIdAndOrganizationId(entityId, organizationId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves audit history by eventType and organizationId.
     *
     * @param eventType      The type of event.
     * @param organizationId The ID of the organization.
     * @return A list of audit entries as response DTOs.
     */
    public List<AuditResponseDTO> getAuditHistoryByEventType(String eventType, String organizationId) {
        logger.info("Retrieving audit history for eventType: {}, organizationId: {}", eventType, organizationId);
        return auditRepository.findByEventTypeAndOrganizationId(eventType, organizationId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves audit history by date range and organizationId.
     *
     * @param startDate      The start of the date range.
     * @param endDate        The end of the date range.
     * @param organizationId The ID of the organization.
     * @return A list of audit entries as response DTOs.
     */
    public List<AuditResponseDTO> getAuditHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate, String organizationId) {
        logger.info("Retrieving audit history for date range: {} to {}, organizationId: {}", startDate, endDate, organizationId);
        return auditRepository.findByTimestampBetweenAndOrganizationId(startDate, endDate, organizationId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves audit history by entityId, eventType, and organizationId.
     *
     * @param entityId       The unique identifier of the entity.
     * @param eventType      The type of event.
     * @param organizationId The ID of the organization.
     * @return A list of audit entries as response DTOs.
     */
    public List<AuditResponseDTO> getAuditHistoryByEntityIdAndEventType(Long entityId, String eventType, String organizationId) {
        logger.info("Retrieving audit history for entityId: {}, eventType: {}, organizationId: {}", entityId, eventType, organizationId);
        return auditRepository.findByEntityIdAndEventTypeAndOrganizationId(
            entityId,
            eventType,
            organizationId
        )
        .stream()
        .map(this::mapToResponseDTO)
        .collect(Collectors.toList());
    }

    /**
     * Rejects any attempt to update an audit entry.
     */
    public void rejectUpdate() {
        logger.error("Attempt to update an immutable audit record");
        throw new AuditImmutableException("Audit records are immutable and cannot be updated.");
    }

    /**
     * Rejects any attempt to delete an audit entry.
     */
    public void rejectDelete() {
        logger.error("Attempt to delete an immutable audit record");
        throw new AuditImmutableException("Audit records are immutable and cannot be deleted.");
    }

    /**
     * Maps an AuditEntry entity to an AuditResponseDTO.
     *
     * @param auditEntry The AuditEntry entity to be mapped.
     * @return The mapped AuditResponseDTO.
     */
    private AuditResponseDTO mapToResponseDTO(AuditEntry auditEntry) {
        return new AuditResponseDTO(
                auditEntry.getId(),
                auditEntry.getEventType(),
                auditEntry.getEntityType(),
                auditEntry.getEntityId(),
                auditEntry.getActorUserId(),
                auditEntry.getOrganizationId(),
                auditEntry.getPreviousStateSnapshot(),
                auditEntry.getNewStateSnapshot(),
                auditEntry.getTimestamp()
        );
    }
}