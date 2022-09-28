package com.fastcampus.finalproject.config.security.jwt.provider;

import com.fastcampus.finalproject.service.auth.dto.NativeUserContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPwEncoder;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPwEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPwEncoder = bCryptPwEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String id = authentication.getName();
        String password = authentication.getCredentials().toString();

        NativeUserContext nativeUserContext = (NativeUserContext) userDetailsService.loadUserByUsername(id);

        if (!bCryptPwEncoder.matches(password, nativeUserContext.getPassword())) {
            throw new BadCredentialsException("로그인 시도 사용자가 제공한 비밀번호와 사용자 비밀번호가 불일치함");
        }

        return new UsernamePasswordAuthenticationToken(nativeUserContext.getUid(), null, nativeUserContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
