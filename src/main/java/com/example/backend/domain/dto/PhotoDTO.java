package com.example.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PhotoDTO {
    private Long id;
    @NotEmpty(message = "URL cannot be empty")
    @Size(max = 255, message = "URL should not exceed 255 characters")
    private String url;

    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @NotEmpty(message = "Title cannot be empty")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    private String title;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
