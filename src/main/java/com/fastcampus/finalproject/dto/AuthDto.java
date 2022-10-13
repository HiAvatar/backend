package com.fastcampus.finalproject.dto;

import lombok.Getter;
import lombok.Setter;

public class AuthDto {

    @Getter
    public static class JwtDto {
        private final String type;
        private final String accessToken;
        private final String refreshToken;

        public JwtDto(String accessToken, String refreshToken) {
            this.type = "Bearer";
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    /**
     * 사용자에 대한 ROLE(ADMIN,USER) 추가 시 사용될 Dto
     */
    @Getter
    public static class JwtAuthorizationDenialDto {
        private final String authResult;
        private final String errorCode;
        private final String errorMessage;

        public JwtAuthorizationDenialDto(String errorCode, String errorMessage) {
            this.authResult = "ACCESS_DENIED";
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }

    @Getter
    @Setter
    public static class AuthenticationDto {
        private String id;
        private String password;
    }
}
