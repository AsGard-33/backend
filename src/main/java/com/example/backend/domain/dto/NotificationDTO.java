package com.example.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class NotificationDTO {
    private Long id;
    @NotEmpty(message = "Message cannot be empty")
    private String message;

    @NotEmpty(message = "Type cannot be empty")
    private String type;

    @NotEmpty(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    public NotificationDTO() {}

    public NotificationDTO(Long id, String message, String type, String status, Long userId) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.status = status;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
