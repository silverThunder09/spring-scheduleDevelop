package com.scheduledevelop.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final Long userId;
    private final String username;
    private final String email;
    private final String message;

    public LoginResponseDto(Long userId, String username, String email, String message) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.message = message;
    }
}

