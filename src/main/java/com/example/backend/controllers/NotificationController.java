package com.example.backend.controllers;

import com.example.backend.domain.dto.NotificationDTO;
import com.example.backend.domain.entity.Notification;
import com.example.backend.services.NotificationService;
import com.example.backend.utils.NotificationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
@Validated
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationConverter notificationConverter;

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        Notification notification = notificationConverter.toEntity(notificationDTO);
        Notification savedNotification = notificationService.createNotification(notification);
        NotificationDTO savedNotificationDTO = notificationConverter.toDTO(savedNotification);
        return ResponseEntity.ok(savedNotificationDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsForUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        List<NotificationDTO> notificationDTOs = notifications.stream().map(notificationConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(notificationDTOs);
    }
// menyaetsya status(prochitano , ne prochitano)
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> updateNotificationStatus(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO) {
        Notification updatedNotification = notificationService.updateNotificationStatus(id, notificationDTO.getStatus());
        NotificationDTO updatedNotificationDTO = notificationConverter.toDTO(updatedNotification);
        return ResponseEntity.ok(updatedNotificationDTO);
    }
}
