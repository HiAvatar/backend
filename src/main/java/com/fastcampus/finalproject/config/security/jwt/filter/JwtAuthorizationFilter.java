package com.fastcampus.finalproject.config.security.jwt.filter;

import com.fastcampus.finalproject.config.security.jwt.utils.AccessTokenUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final AccessTokenUtility accessTokenUtility;

    public JwtAuthorizationFilter(AccessTokenUtility accessTokenUtility) {
        this.accessTokenUtility = accessTokenUtility;
    }

    @Override
    public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String jwt = resolveTokenFromHttpHeader(servletRequest);
        String requestURI = servletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && accessTokenUtility.validateToken(jwt)) {

            Authentication authentication = accessTokenUtility.getAuthenticationTokenFromJwt(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.debug("Authentication info stored in the security context. Principal: '{}', URI: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("Token not valid. Failed to create an Authentication Token. URI: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveTokenFromHttpHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
