package com.example.backend.controllers;

import com.example.backend.domain.dto.NotificationDTO;
import com.example.backend.domain.entity.Notification;
import com.example.backend.services.NotificationService;
import com.example.backend.utils.NotificationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationConverter notificationConverter;

    @InjectMocks
    private NotificationController notificationController;

    private Notification notification;
    private NotificationDTO notificationDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test message");
        notification.setType("INFO");
        notification.setStatus("UNREAD");

        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setMessage("Test message");
        notificationDTO.setType("INFO");
        notificationDTO.setStatus("UNREAD");
    }

    @Test
    public void testCreateNotification() {
        when(notificationConverter.toEntity(any(NotificationDTO.class))).thenReturn(notification);
        when(notificationService.createNotification(any(Notification.class))).thenReturn(notification);
        when(notificationConverter.toDTO(any(Notification.class))).thenReturn(notificationDTO);

        ResponseEntity<NotificationDTO> response = notificationController.createNotification(notificationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notificationDTO, response.getBody());
        verify(notificationService, times(1)).createNotification(notification);
    }

    @Test
    public void testGetNotificationsForUser() {
        when(notificationService.getNotificationsByUserId(anyLong())).thenReturn(Arrays.asList(notification));
        when(notificationConverter.toDTO(any(Notification.class))).thenReturn(notificationDTO);

        ResponseEntity<List<NotificationDTO>> response = notificationController.getNotificationsForUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(notificationDTO, response.getBody().get(0));
        verify(notificationService, times(1)).getNotificationsByUserId(1L);
    }

    @Test
    public void testUpdateNotificationStatus() {
        when(notificationService.updateNotificationStatus(anyLong(), anyString())).thenReturn(notification);
        when(notificationConverter.toDTO(any(Notification.class))).thenReturn(notificationDTO);

        ResponseEntity<NotificationDTO> response = notificationController.updateNotificationStatus(1L, notificationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notificationDTO, response.getBody());
        verify(notificationService, times(1)).updateNotificationStatus(1L, notificationDTO.getStatus());
    }
}
