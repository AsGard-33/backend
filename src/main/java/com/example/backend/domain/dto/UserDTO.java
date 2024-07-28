package com.example.backend.domain.dto;

import com.example.backend.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Objects;

public class UserDTO {

    private Long id;

    @NotEmpty(message = "Username is mandatory")
    @Size(max = 50, message = "Username should not exceed 50 characters")
    private String username;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email should not exceed 100 characters")
    private String email;

    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.avatarUrl = user.getAvatarUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) &&
                Objects.equals(username, userDTO.username) &&
                Objects.equals(email, userDTO.email) &&
                Objects.equals(avatarUrl, userDTO.avatarUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, avatarUrl);
    }
}
