package com.fastcampus.finalproject.config.security.jwt.filter;

import com.fastcampus.finalproject.dto.ErrorResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionTranslationFilter extends OncePerRequestFilter {

    private static final String ERROR_CODE_ACCESS_TOKEN_EXPIRED = "TOKEN_EXPIRED";
    private static final String ERROR_MESSAGE_ACCESS_TOKEN_EXPIRED = "만료된 접근 토큰입니다.";
    private static final String ERROR_CODE_INVALID_ACCESS_TOKEN = "TOKEN_INVALID";
    private static final String ERROR_MESSAGE_INVALID_ACCESS_TOKEN = "접근 토큰이 유효하지 않습니다";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        try {

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SecurityException | IllegalArgumentException exception) {
            ErrorResponseWrapper errorResponse;

            if (exception instanceof ExpiredJwtException) {
                errorResponse = new ErrorResponseWrapper(ERROR_CODE_ACCESS_TOKEN_EXPIRED, ERROR_MESSAGE_ACCESS_TOKEN_EXPIRED).unauthorized();
            } else {
                errorResponse = new ErrorResponseWrapper(ERROR_CODE_INVALID_ACCESS_TOKEN, ERROR_MESSAGE_INVALID_ACCESS_TOKEN).unauthorized();
            }

            String serializedErrorResponse = new ObjectMapper().writeValueAsString(errorResponse);

            response.getWriter().write(serializedErrorResponse);
            response.getWriter().flush();
        }
    }
}
