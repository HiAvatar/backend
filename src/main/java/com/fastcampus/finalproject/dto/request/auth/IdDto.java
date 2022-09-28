package com.fastcampus.finalproject.dto.request.auth;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class IdDto {

    @NotNull
    private String id;

    public IdDto() {
    }

    public IdDto(String id) {
        this.id = id;
    }
}
