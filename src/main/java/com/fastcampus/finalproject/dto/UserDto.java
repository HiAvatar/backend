package com.fastcampus.finalproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class UserDto {

    @Getter
    @Setter
    public static class SignUpRequest {

        @NotNull
        private String id;

        @NotNull
        private String password;
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SignUpResponse {

        @JsonFormat
        private final String id;

        public SignUpResponse(String id) {
            this.id = id;
        }
    }

    @Getter
    @Setter
    public static class DuplicateIdRequest {

        @NotNull
        private String id;
    }

    @Getter
    public static class DuplicateIdResponse {

        private final boolean isIdAvailable;

        public DuplicateIdResponse(boolean isIdAvailable) {
            this.isIdAvailable = isIdAvailable;
        }
    }

    @Data
    public static class TokenPairResponse {

        private String accessToken;
        private String refreshToken;
    }

    @Data
    public static class NewPasswordRequest {

        @NotNull
        private String newPassword;
    }

    @Data
    public static class UserInfoResponse {

        private String id;

        public UserInfoResponse(String id) {
            this.id = id;
        }
    }
}
