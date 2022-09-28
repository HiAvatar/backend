package com.fastcampus.finalproject.dto.response;

import lombok.Getter;

@Getter
public class JwtAuthorizationDenialDto {
    private final String authResult;
    private final String errorCode;
    private final String errorMessage;

    public JwtAuthorizationDenialDto(String errorCode, String errorMessage) {
        this.authResult = "ACCESS_DENIED";
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
