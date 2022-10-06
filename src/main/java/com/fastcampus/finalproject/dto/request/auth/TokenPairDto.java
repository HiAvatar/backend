package com.fastcampus.finalproject.dto.request.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenPairDto {
    private String accessToken;
    private String refreshToken;

    public TokenPairDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
