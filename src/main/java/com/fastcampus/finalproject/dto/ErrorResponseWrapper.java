package com.fastcampus.finalproject.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseWrapper {

    private int code;
    private String status;
    private String error;
    private String message;

    public ErrorResponseWrapper badRequest(String error, String message) {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        this.error = error;
        this.message = message;
        return this;
    }
}
