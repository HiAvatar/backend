package com.fastcampus.finalproject.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseWrapper {

    private int code;
    private String status;
    private String error;
    private String message;

    public ErrorResponseWrapper(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public ErrorResponseWrapper badRequest() {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        this.error = error;
        this.message = message;
        return this;
    }

    public ErrorResponseWrapper unauthorized() {
        this.code = HttpStatus.UNAUTHORIZED.value();
        this.status = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        return this;
    }

    public ErrorResponseWrapper forbidden() {
        this.code = HttpStatus.FORBIDDEN.value();
        this.status = HttpStatus.FORBIDDEN.getReasonPhrase();
        return this;
    }
}
