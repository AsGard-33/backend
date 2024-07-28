package com.example.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;

public class AvatarUpdateDTO {

    @NotEmpty(message = "Avatar URL is mandatory")
    private String avatarUrl;

    // Getters and Setters

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
