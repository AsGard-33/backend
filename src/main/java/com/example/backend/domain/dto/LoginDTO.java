package com.example.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;


public class LoginDTO {
    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    public @NotBlank(message = "Username is mandatory") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is mandatory") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Password is mandatory") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is mandatory") String password) {
        this.password = password;
    }
}
