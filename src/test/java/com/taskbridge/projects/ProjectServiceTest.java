package com.taskbridge.projects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskbridge.audit.dto.AuditRequestDTO;
import com.taskbridge.audit.service.AuditService;
import com.taskbridge.notifications.entity.Notification;
import com.taskbridge.notifications.service.NotificationService;
import com.taskbridge.projects.dto.ProjectRequestDTO;
import com.taskbridge.projects.entity.Project;
import com.taskbridge.projects.exception.ProjectNotFoundException;
import com.taskbridge.projects.repository.ProjectRepository;
import com.taskbridge.projects.service.ProjectService;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void shouldCreateAuditAndNotificationWhenProjectCreated() {

        ProjectRequestDTO request =
                new ProjectRequestDTO(
                        "Project Alpha",
                        "First Project",
                        "CREATED",
                        101L
                );

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("Project Alpha");
        savedProject.setDescription("First Project");
        savedProject.setStatus("CREATED");
        savedProject.setTeamId(101L);
        savedProject.setTenantId("ORG001");

        when(projectRepository.save(any(Project.class)))
                .thenReturn(savedProject);

        projectService.createProject(request, "ORG001");

        ArgumentCaptor<AuditRequestDTO> auditCaptor =
                ArgumentCaptor.forClass(AuditRequestDTO.class);

        verify(auditService, times(1))
                .createAuditEntry(auditCaptor.capture());

        AuditRequestDTO auditRequest = auditCaptor.getValue();

        assertEquals("CREATED", auditRequest.eventType());
        assertEquals("PROJECT", auditRequest.entityType());
        assertEquals(1L, auditRequest.entityId());
        assertEquals("SYSTEM", auditRequest.actorUserId());
        assertEquals("ORG001", auditRequest.organizationId());

        verify(notificationService, times(1))
                .createNotification(any(Notification.class));
    }

    @Test
    void shouldCreateAuditAndNotificationWhenStatusUpdated() {

        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Project Alpha");
        existingProject.setStatus("CREATED");
        existingProject.setTeamId(101L);
        existingProject.setTenantId("ORG001");

        when(projectRepository.findByIdAndTenantId(
                1L,
                "ORG001"))
                .thenReturn(Optional.of(existingProject));

        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        projectService.updateProjectStatus(
                1L,
                "CLOSED",
                "ORG001");

        ArgumentCaptor<AuditRequestDTO> auditCaptor =
                ArgumentCaptor.forClass(AuditRequestDTO.class);

        verify(auditService)
                .createAuditEntry(auditCaptor.capture());

        AuditRequestDTO auditRequest = auditCaptor.getValue();

        assertEquals("UPDATED", auditRequest.eventType());
        assertEquals("PROJECT", auditRequest.entityType());
        assertEquals("CREATED", auditRequest.previousStateSnapshot());
        assertEquals("CLOSED", auditRequest.newStateSnapshot());

        verify(notificationService)
                .createNotification(any(Notification.class));
    }

    @Test
    void shouldCreateAuditAndNotificationBeforeDelete() {

        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Project Alpha");
        existingProject.setStatus("CLOSED");
        existingProject.setTeamId(101L);
        existingProject.setTenantId("ORG001");

        when(projectRepository.findByIdAndTenantId(
                1L,
                "ORG001"))
                .thenReturn(Optional.of(existingProject));

        projectService.deleteProject(
                1L,
                "ORG001");

        ArgumentCaptor<AuditRequestDTO> auditCaptor =
                ArgumentCaptor.forClass(AuditRequestDTO.class);

        verify(auditService)
                .createAuditEntry(auditCaptor.capture());

        AuditRequestDTO auditRequest = auditCaptor.getValue();

        assertEquals("CLOSED", auditRequest.eventType());
        assertEquals("PROJECT", auditRequest.entityType());

        verify(notificationService)
                .createNotification(any(Notification.class));

        verify(projectRepository)
                .delete(existingProject);
    }

    @Test
    void shouldThrowProjectNotFoundExceptionWhenProjectMissing() {

        when(projectRepository.findByIdAndTenantId(
                999L,
                "ORG001"))
                .thenReturn(Optional.empty());

        assertThrows(
        ProjectNotFoundException.class,
        () -> projectService.updateProjectStatus(
                999L,
                "CLOSED",
                "ORG001")
        );

        verify(auditService, never())
                .createAuditEntry(any());

        verify(notificationService, never())
                .createNotification(any());
    }
}
 