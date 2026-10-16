package com.tas.auth.dto;

public class RegisterResponseDTO {

private Long id;
    private String username;

    public RegisterResponseDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }
    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
}
