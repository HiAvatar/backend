package com.fastcampus.finalproject.config.security.jwt.handler;

import com.fastcampus.finalproject.dto.ErrorResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String ERROR_CODE_NO_TOKEN = "NO_TOKEN";
    private static final String ERROR_MESSAGE_NO_TOKEN = "요청에 필요한 토큰이 없습니다.";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorResponseWrapper errorResponse = new ErrorResponseWrapper(ERROR_CODE_NO_TOKEN, ERROR_MESSAGE_NO_TOKEN).unauthorized();
        String serializedErrorResponse = new ObjectMapper().writeValueAsString(errorResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(serializedErrorResponse);
        response.getWriter().flush();
    }
}
