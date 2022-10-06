package com.fastcampus.finalproject.exception.token;

public class AccessTokenInvalidException extends RuntimeException{
    public AccessTokenInvalidException(String message) {
        super(message);
    }
}
