package com.fastcampus.finalproject.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class NewPasswordDto {

    @NotNull
    private final String newPassword;

    public NewPasswordDto(String newPassword) {
        this.newPassword = newPassword;
    }
}
