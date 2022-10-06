package com.fastcampus.finalproject.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewPasswordDto {

    @NotNull
    private String newPassword;

    public NewPasswordDto(String newPassword) {
        this.newPassword = newPassword;
    }
}
