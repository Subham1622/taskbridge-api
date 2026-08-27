package com.taskbridge.notifications;

import com.taskbridge.notifications.dto.NotificationResponseDTO;
import com.taskbridge.notifications.entity.Notification;
import com.taskbridge.notifications.repository.NotificationRepository;
import com.taskbridge.notifications.service.NotificationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldRetrieveNotificationsByRecipient() {

        Notification notification =
                new Notification(
                        "TEAM_101",
                        1L,
                        "CREATED",
                        "Project created: Project Alpha",
                        "ORG001"
                );

        when(notificationRepository.findByRecipientUserIdAndOrganizationId(
                "TEAM_101",
                "ORG001"))
                .thenReturn(List.of(notification));

        List<NotificationResponseDTO> results =
                notificationService.getNotificationsByRecipient(
                        "TEAM_101",
                        "ORG001"
                );

        assertEquals(1, results.size());
        assertEquals("TEAM_101", results.get(0).recipientUserId());
    }

    @Test
    void shouldRetrieveUnreadNotifications() {

        Notification notification =
                new Notification(
                        "TEAM_101",
                        1L,
                        "UPDATED",
                        "Project updated: Project Alpha",
                        "ORG001"
                );

        when(notificationRepository.findUnreadByRecipientUserIdAndOrganizationId(
                "TEAM_101",
                "ORG001"))
                .thenReturn(List.of(notification));

        List<NotificationResponseDTO> results =
                notificationService.getUnreadNotifications(
                        "TEAM_101",
                        "ORG001"
                );

        assertEquals(1, results.size());
        assertFalse(results.get(0).readStatus());
    }

    @Test
    void shouldRetrieveNotificationsByProject() {

        Notification notification =
                new Notification(
                        "TEAM_101",
                        1L,
                        "UPDATED",
                        "Project updated: Project Alpha",
                        "ORG001"
                );

        when(notificationRepository.findByProjectIdAndOrganizationId(
                1L,
                "ORG001"))
                .thenReturn(List.of(notification));

        List<NotificationResponseDTO> results =
                notificationService.getNotificationsByProject(
                        1L,
                        "ORG001"
                );

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).projectId());
    }

    @Test
    void shouldRetrieveNotificationsByEventType() {

        Notification notification =
                new Notification(
                        "TEAM_101",
                        1L,
                        "CLOSED",
                        "Project closed: Project Alpha",
                        "ORG001"
                );

        when(notificationRepository.findByEventTypeAndOrganizationId(
                "CLOSED",
                "ORG001"))
                .thenReturn(List.of(notification));

        List<NotificationResponseDTO> results =
                notificationService.getNotificationsByEventType(
                        "CLOSED",
                        "ORG001"
                );

        assertEquals(1, results.size());
        assertEquals("CLOSED", results.get(0).eventType());
    }

    @Test
    void shouldMarkNotificationAsRead() {

        Notification notification =
                new Notification(
                        "TEAM_101",
                        1L,
                        "CREATED",
                        "Project created: Project Alpha",
                        "ORG001"
                );

        when(notificationRepository.findByIdAndOrganizationId(
                1L,
                "ORG001"))
                .thenReturn(Optional.of(notification));

        notificationService.markNotificationAsRead(
                1L,
                "ORG001"
        );

        assertTrue(notification.getReadStatus());

        verify(notificationRepository)
                .save(notification);
    }
}