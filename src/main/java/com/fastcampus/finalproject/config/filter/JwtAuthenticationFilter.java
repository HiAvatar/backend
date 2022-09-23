package com.fastcampus.finalproject.config.filter;

import com.fastcampus.finalproject.dto.request.AuthenticationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String id;
        String password;

        try {
            AuthenticationDto authInfo = new ObjectMapper().readValue(request.getInputStream(), AuthenticationDto.class);
            id = authInfo.getId();
            password = authInfo.getPassword();
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password);

        return this.getAuthenticationManager().authenticate(authToken);
    }


}
