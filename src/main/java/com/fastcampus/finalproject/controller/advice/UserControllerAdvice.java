package com.fastcampus.finalproject.controller.advice;

import com.fastcampus.finalproject.controller.UserController;
import com.fastcampus.finalproject.dto.ErrorResponseWrapper;
import com.fastcampus.finalproject.exception.token.AccessTokenStillValidException;
import com.fastcampus.finalproject.exception.token.AccessTokenInvalidException;
import com.fastcampus.finalproject.exception.token.RefreshTokenInvalidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerAdvice {

    @ExceptionHandler(value = AccessTokenStillValidException.class)
    public ErrorResponseWrapper handleAccessTokenStillValidException(AccessTokenStillValidException ex) {
        ErrorResponseWrapper errorResponse = new ErrorResponseWrapper("ACCESS_TOKEN_STILL_VALID", ex.getMessage());
        return errorResponse.badRequest();
    }

    @ExceptionHandler(value = AccessTokenInvalidException.class)
    public ErrorResponseWrapper handleAccessTokenInvalidException(AccessTokenInvalidException ex) {
        ErrorResponseWrapper errorResponse = new ErrorResponseWrapper("ACCESS_TOKEN_INVALID", ex.getMessage());
        return errorResponse.badRequest();
    }

    @ExceptionHandler(value = RefreshTokenInvalidException.class)
    public ErrorResponseWrapper handleRefreshTokenInvalidException(RefreshTokenInvalidException ex) {
        ErrorResponseWrapper errorResponse = new ErrorResponseWrapper("REFRESH_TOKEN_INVALID", ex.getMessage());
        return errorResponse.badRequest();
    }
}
