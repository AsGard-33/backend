package com.example.backend.services;

import com.example.backend.domain.entity.Notification;
import com.example.backend.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test message");
        notification.setType("INFO");
        notification.setStatus("UNREAD");
    }

    @Test
    public void testCreateNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification createdNotification = notificationService.createNotification(notification);

        assertEquals(notification, createdNotification);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    public void testGetNotificationsByUserId() {
        when(notificationRepository.findByUserId(anyLong())).thenReturn(Arrays.asList(notification));

        List<Notification> notifications = notificationService.getNotificationsByUserId(1L);

        assertEquals(1, notifications.size());
        assertEquals(notification, notifications.get(0));
        verify(notificationRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testUpdateNotificationStatus() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification updatedNotification = notificationService.updateNotificationStatus(1L, "READ");

        assertEquals("READ", updatedNotification.getStatus());
        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    public void testUpdateNotificationStatus_NotFound() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.updateNotificationStatus(1L, "READ"));
        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(0)).save(any(Notification.class));
    }
}
