package com.fastcampus.finalproject.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SignUpInfoDto {

    @NotNull
    private String id;

    @NotNull
    private String password;


    public SignUpInfoDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public SignUpInfoDto(String id) {
        this.id = id;
    }


}
