package com.fastcampus.finalproject.config.security.jwt.handler;

import com.fastcampus.finalproject.config.security.jwt.utils.AccessTokenUtility;
import com.fastcampus.finalproject.config.security.jwt.utils.RefreshTokenUtility;
import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.response.JwtDto;
import com.fastcampus.finalproject.entity.UserRefreshToken;
import com.fastcampus.finalproject.repository.RefreshTokenRepository;
import com.fastcampus.finalproject.service.auth.dto.OAuthUserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AccessTokenUtility accessTokenUtility;
    private final RefreshTokenUtility refreshTokenUtility;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationSuccessHandler(AccessTokenUtility accessTokenUtility, RefreshTokenUtility refreshTokenUtility, RefreshTokenRepository refreshTokenRepository) {
        this.accessTokenUtility = accessTokenUtility;
        this.refreshTokenUtility = refreshTokenUtility;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            Long userUid = ((OAuthUserContext) authentication.getPrincipal()).getUserUid();
            authentication = new UsernamePasswordAuthenticationToken(userUid, authentication.getAuthorities());
        }

        String accessToken = accessTokenUtility.createJsonWebToken(authentication);
        String refreshToken = refreshTokenUtility.createRefreshJsonWebToken(authentication);

        JwtDto jwtDto = new JwtDto(accessToken, refreshToken);
        String serializedJwtDto = new ObjectMapper().writeValueAsString(new ResponseWrapper<>(jwtDto).ok());

        refreshTokenRepository.save(new UserRefreshToken((Long) authentication.getPrincipal(), refreshToken));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(serializedJwtDto);
        response.getWriter().flush();
    }
}
