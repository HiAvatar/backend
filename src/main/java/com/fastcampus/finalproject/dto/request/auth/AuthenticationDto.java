package com.fastcampus.finalproject.dto.request.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthenticationDto {
    private String id;
    private String password;

    public AuthenticationDto(String id, String password) {
        this.id = id;
        this.password = password;
    }


}
