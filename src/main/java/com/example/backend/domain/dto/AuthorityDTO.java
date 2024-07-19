package com.example.backend.domain.dto;

public class AuthorityDTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthorityDTO(String name) {
        this.name = name;
    }
}
