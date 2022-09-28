package com.fastcampus.finalproject.config.security;

import com.fastcampus.finalproject.config.security.jwt.filter.JwtAuthenticationFilter;
import com.fastcampus.finalproject.config.security.jwt.filter.JwtAuthorizationFilter;
import com.fastcampus.finalproject.config.security.jwt.filter.JwtExceptionTranslationFilter;
import com.fastcampus.finalproject.config.security.jwt.handler.JwtAuthenticationFailureHandler;
import com.fastcampus.finalproject.config.security.jwt.handler.JwtAuthenticationSuccessHandler;
import com.fastcampus.finalproject.config.security.jwt.provider.CustomAuthenticationProvider;
import com.fastcampus.finalproject.config.security.jwt.utils.AccessTokenUtility;
import com.fastcampus.finalproject.config.security.jwt.utils.RefreshTokenUtility;
import com.fastcampus.finalproject.repository.RefreshTokenRepository;
import com.fastcampus.finalproject.service.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final AccessTokenUtility accessTokenUtility;
    private final RefreshTokenUtility refreshTokenUtility;
    private final RefreshTokenRepository refreshTokenRepository;

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(AccessTokenUtility accessTokenUtility, RefreshTokenUtility refreshTokenUtility, RefreshTokenRepository refreshTokenRepository, CustomUserDetailsService customUserDetailsService) {
        this.accessTokenUtility = accessTokenUtility;
        this.refreshTokenUtility = refreshTokenUtility;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain customFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .formLogin().disable();

        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
                .authorizeRequests()
                .antMatchers("/sign-up", "/sign-up/**").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();

        httpSecurity
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), JwtAuthorizationFilter.class)
                .addFilterBefore(jwtExceptionTranslationFilter(), JwtAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public Filter jwtExceptionTranslationFilter() {
        return new JwtExceptionTranslationFilter();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();

        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());

        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(accessTokenUtility);
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authProviders = new ArrayList<>();
        authProviders.add(customAuthenticationProvider());

        return new ProviderManager(authProviders);
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, bCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler(accessTokenUtility, refreshTokenUtility, refreshTokenRepository);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}


