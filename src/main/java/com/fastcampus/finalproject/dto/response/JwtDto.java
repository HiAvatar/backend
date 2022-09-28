package com.fastcampus.finalproject.dto.response;

import lombok.Getter;

@Getter
public class JwtDto {
    private final String type;
    private final String accessToken;
    private final String refreshToken;

    public JwtDto(String accessToken, String refreshToken) {
        this.type = "Bearer";
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
