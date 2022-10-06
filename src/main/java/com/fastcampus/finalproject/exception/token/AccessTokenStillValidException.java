package com.fastcampus.finalproject.exception.token;

public class AccessTokenStillValidException extends RuntimeException{
    public AccessTokenStillValidException(String message) {
        super(message);
    }
}
