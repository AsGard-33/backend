package com.example.backend.domain.dto;

public class LoginResponseDTO {
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public LoginResponseDTO(String access_token, String refreshToken) {
        this.access_token = access_token;
    }
}

