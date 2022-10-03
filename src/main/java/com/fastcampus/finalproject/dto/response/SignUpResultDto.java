package com.fastcampus.finalproject.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpResultDto {
    @JsonFormat
    private final String id;

    public SignUpResultDto(String id) {
        this.id = id;
    }
}
