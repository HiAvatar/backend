package com.fastcampus.finalproject.config.jwt.handler;

import com.fastcampus.finalproject.config.jwt.utils.AccessTokenProvider;
import com.fastcampus.finalproject.config.jwt.utils.RefreshTokenProvider;
import com.fastcampus.finalproject.dto.response.JwtDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    public JwtAuthenticationSuccessHandler(AccessTokenProvider accessTokenProvider, RefreshTokenProvider refreshTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String accessToken = accessTokenProvider.createJsonWebToken(authentication);
        String refreshToken = refreshTokenProvider.createRefreshJsonWebToken(authentication);

        JwtDto jwtDto = new JwtDto(accessToken, refreshToken);
        String serializedJwtDto = new ObjectMapper().writeValueAsString(jwtDto);

        response.getWriter().write(serializedJwtDto);
        response.getWriter().flush();
    }
}
