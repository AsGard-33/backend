package com.example.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LocationDTO {

    private Long id;
    @NotEmpty(message = "Title is mandatory")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    private String title;

    @NotEmpty(message = "Description is mandatory")
    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @NotEmpty(message = "Image URL is mandatory")
    @Size(max = 200, message = "Image URL should not exceed 200 characters")
    private String image;

    @NotEmpty(message = "Coordinates are mandatory")
    @Size(max = 50, message = "Coordinates should not exceed 50 characters")
    private String coordinates;

    public LocationDTO() {}

    public LocationDTO(Long id, String title, String description, String image, String coordinates) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.coordinates = coordinates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
