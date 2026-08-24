package com.taskbridge.audit.controller;

import com.taskbridge.audit.dto.AuditRequestDTO;
import com.taskbridge.audit.dto.AuditResponseDTO;

import com.taskbridge.audit.service.AuditService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing audit records in the TaskBridge Notification & Audit Service.
 * <p>
 * This controller enforces multi-tenant SaaS requirements, ensuring all requests are scoped
 * to the tenant context provided via the X-Tenant-ID header.
 * </p>
 */
@RestController
@RequestMapping("/audit")
@Validated
public class AuditController {

    private static final Logger logger = LoggerFactory.getLogger(AuditController.class);

    private final AuditService auditService;

    /**
     * Constructor for AuditController.
     *
     * @param auditService The service for managing audit records.
     */
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Creates a new audit entry.
     *
     * @param requestDTO The DTO containing the details of the audit entry to be created.
     * @return The created audit entry as a response DTO.
     */
    @PostMapping
    public ResponseEntity<AuditResponseDTO> createAuditEntry(@Valid @RequestBody AuditRequestDTO requestDTO) {
        logger.info("Creating audit entry for entityId: {}, organizationId: {}", requestDTO.entityId(), requestDTO.organizationId());
        AuditResponseDTO responseDTO = auditService.createAuditEntry(requestDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }

    /**
     * Retrieves audit history for a specific entity within the tenant context.
     *
     * @param entityId       The unique identifier of the entity.
     * @param tenantId       The tenant ID from the X-Tenant-ID header.
     * @return A list of audit entries as response DTOs.
     */
    @GetMapping("/{entityId}")
    public ResponseEntity<List<AuditResponseDTO>> getAuditHistoryByEntityId(
            @PathVariable Long entityId,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        logger.info("Retrieving audit history for entityId: {}, tenantId: {}", entityId, tenantId);
        List<AuditResponseDTO> responseDTOs = auditService.getAuditHistoryByEntityId(entityId, tenantId);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Retrieves audit history for a specific entity and event type within the tenant context.
     *
     * @param entityId       The unique identifier of the entity.
     * @param eventType      The type of event.
     * @param tenantId       The tenant ID from the X-Tenant-ID header.
     * @return A list of audit entries as response DTOs.
     */
    @GetMapping("/{entityId}/event/{eventType}")
    public ResponseEntity<List<AuditResponseDTO>> getAuditHistoryByEntityIdAndEventType(
            @PathVariable Long entityId,
            @PathVariable String eventType,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        logger.info("Retrieving audit history for entityId: {}, eventType: {}, tenantId: {}", entityId, eventType, tenantId);
        List<AuditResponseDTO> responseDTOs = auditService.getAuditHistoryByEntityIdAndEventType(entityId, eventType, tenantId);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Retrieves audit history within a specific date range for the tenant context.
     *
     * @param startDate      The start of the date range.
     * @param endDate        The end of the date range.
     * @param tenantId       The tenant ID from the X-Tenant-ID header.
     * @return A list of audit entries as response DTOs.
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<AuditResponseDTO>> getAuditHistoryByDateRange(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam LocalDateTime endDate,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        logger.info("Retrieving audit history for date range: {} to {}, tenantId: {}", startDate, endDate, tenantId);
        List<AuditResponseDTO> responseDTOs = auditService.getAuditHistoryByDateRange(startDate, endDate, tenantId);
        return ResponseEntity.ok(responseDTOs);
    }
}