package com.fastcampus.finalproject.config.security.jwt.handler;

import com.fastcampus.finalproject.dto.ErrorResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String ERROR_CODE_LOGIN_FAILURE = "AUTHENTICATION_FAILED";
    private static final String ERROR_MESSAGE_LOGIN_FAILURE = "아이디가 존재하지 않거나 비밀번호가 올바르지 않습니다";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        ErrorResponseWrapper errorResponse = new ErrorResponseWrapper(ERROR_CODE_LOGIN_FAILURE, ERROR_MESSAGE_LOGIN_FAILURE).unauthorized();
        String serializedErrorResponse = new ObjectMapper().writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(serializedErrorResponse);
        response.getWriter().flush();
    }
}
