package com.taskbridge.audit;

import com.taskbridge.audit.dto.AuditRequestDTO;
import com.taskbridge.audit.dto.AuditResponseDTO;
import com.taskbridge.audit.entity.AuditEntry;
import com.taskbridge.audit.exception.AuditImmutableException;
import com.taskbridge.audit.repository.AuditRepository;
import com.taskbridge.audit.service.AuditService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditService auditService;

    @Test
    void shouldCreateAuditEntry() {

        AuditRequestDTO request =
                new AuditRequestDTO(
                        "CREATED",
                        "PROJECT",
                        1L,
                        "SYSTEM",
                        "ORG001",
                        null,
                        "CREATED"
                );

        AuditEntry savedEntry =
                new AuditEntry(
                        "CREATED",
                        "PROJECT",
                        1L,
                        "SYSTEM",
                        "ORG001",
                        null,
                        "CREATED",
                        LocalDateTime.now()
                );

        when(auditRepository.save(any(AuditEntry.class)))
                .thenReturn(savedEntry);

        AuditResponseDTO response =
                auditService.createAuditEntry(request);

        assertNotNull(response);
        assertEquals("CREATED", response.eventType());
        assertEquals("PROJECT", response.entityType());

        verify(auditRepository, times(1))
                .save(any(AuditEntry.class));
    }

    @Test
    void shouldFilterAuditByEventType() {

        AuditEntry entry =
                new AuditEntry(
                        "UPDATED",
                        "PROJECT",
                        1L,
                        "SYSTEM",
                        "ORG001",
                        "CREATED",
                        "CLOSED",
                        LocalDateTime.now()
                );

        when(auditRepository.findByEntityIdAndEventTypeAndOrganizationId(
                1L,
                "UPDATED",
                "ORG001"))
                .thenReturn(List.of(entry));

        List<AuditResponseDTO> results =
                auditService.getAuditHistoryByEntityIdAndEventType(
                        1L,
                        "UPDATED",
                        "ORG001");

        assertEquals(1, results.size());
        assertEquals("UPDATED", results.get(0).eventType());
    }

    @Test
    void shouldFilterAuditByDate() {

        AuditEntry entry =
                new AuditEntry(
                        "CREATED",
                        "PROJECT",
                        1L,
                        "SYSTEM",
                        "ORG001",
                        null,
                        "CREATED",
                        LocalDateTime.now()
                );

        LocalDateTime start =
                LocalDateTime.now().minusDays(1);

        LocalDateTime end =
                LocalDateTime.now().plusDays(1);

        when(auditRepository.findByTimestampBetweenAndOrganizationId(
                start,
                end,
                "ORG001"))
                .thenReturn(List.of(entry));

        List<AuditResponseDTO> results =
                auditService.getAuditHistoryByDateRange(
                        start,
                        end,
                        "ORG001");

        assertEquals(1, results.size());
        assertEquals("CREATED", results.get(0).eventType());
    }

    @Test
    void shouldRetrieveAuditHistoryByEntityId() {

        AuditEntry entry =
                new AuditEntry(
                        "CREATED",
                        "PROJECT",
                        1L,
                        "SYSTEM",
                        "ORG001",
                        null,
                        "CREATED",
                        LocalDateTime.now()
                );

        when(auditRepository.findByEntityIdAndOrganizationId(
                1L,
                "ORG001"))
                .thenReturn(List.of(entry));

        List<AuditResponseDTO> results =
                auditService.getAuditHistoryByEntityId(
                        1L,
                        "ORG001");

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).entityId());
    }

    @Test
    void shouldPreventAuditDeletion() {

        assertThrows(
                AuditImmutableException.class,
                () -> auditService.rejectDelete()
        );
    }

    @Test
    void shouldPreventAuditUpdate() {

        assertThrows(
                AuditImmutableException.class,
                () -> auditService.rejectUpdate()
        );
    }
}