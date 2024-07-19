package com.example.backend.utils;

import com.example.backend.domain.dto.NotificationDTO;
import com.example.backend.domain.entity.Notification;
import com.example.backend.domain.entity.User;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {

    @Autowired
    private UserService userService;

    public NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getType(),
                notification.getStatus(),
                notification.getUser().getId()
        );
    }

    public Notification toEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setId(notificationDTO.getId());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(notificationDTO.getType());
        notification.setStatus(notificationDTO.getStatus());
        User user = userService.findById(notificationDTO.getUserId());
        notification.setUser(user);
        return notification;
    }
}
