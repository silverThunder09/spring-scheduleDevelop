package com.scheduledevelop.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateRequestDto {

    private String username;
    private String email;
}
