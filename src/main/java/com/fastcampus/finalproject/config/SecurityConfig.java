package com.fastcampus.finalproject.config;

import com.fastcampus.finalproject.config.security.jwt.filter.JwtAuthenticationFilter;
import com.fastcampus.finalproject.config.security.jwt.filter.JwtAuthorizationFilter;
import com.fastcampus.finalproject.config.security.jwt.filter.JwtExceptionTranslationFilter;
import com.fastcampus.finalproject.config.security.jwt.handler.JwtAccessDeniedHandler;
import com.fastcampus.finalproject.config.security.jwt.handler.JwtAuthenticationEntryPoint;
import com.fastcampus.finalproject.config.security.jwt.handler.JwtAuthenticationFailureHandler;
import com.fastcampus.finalproject.config.security.jwt.handler.JwtAuthenticationSuccessHandler;
import com.fastcampus.finalproject.config.security.jwt.provider.CustomAuthenticationProvider;
import com.fastcampus.finalproject.config.security.jwt.utils.AccessTokenUtility;
import com.fastcampus.finalproject.config.security.jwt.utils.RefreshTokenUtility;
import com.fastcampus.finalproject.repository.RefreshTokenRepository;
import com.fastcampus.finalproject.repository.SocialLoginUserRepository;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.service.auth.CustomOAuth2UserService;
import com.fastcampus.finalproject.service.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private final AccessTokenUtility accessTokenUtility;
    private final RefreshTokenUtility refreshTokenUtility;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final SocialLoginUserRepository socialLoginUserRepository;
    private final CustomUserDetailsService userDetailsService;
    private final Environment env;
    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";


    public SecurityConfig(AccessTokenUtility accessTokenUtility, RefreshTokenUtility refreshTokenUtility, RefreshTokenRepository refreshTokenRepository, CustomUserDetailsService customUserDetailsService, UserRepository userRepository, SocialLoginUserRepository socialLoginUserRepository, Environment env) {
        this.accessTokenUtility = accessTokenUtility;
        this.refreshTokenUtility = refreshTokenUtility;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.socialLoginUserRepository = socialLoginUserRepository;
        this.env = env;
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
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        httpSecurity
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), JwtAuthorizationFilter.class)
                .addFilterBefore(jwtExceptionTranslationFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(oAuth2AuthorizationRequestRedirectFilter(), JwtExceptionTranslationFilter.class)
                .addFilterBefore(oAuth2LoginAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class);

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
        authProviders.add(new OAuth2LoginAuthenticationProvider(defaultAuthorizationCodeTokenResponseClient(), customOAuth2UserService()));

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

    // OAuth 2.0 Related Beans
    @Bean
    public OAuth2AuthorizationRequestRedirectFilter oAuth2AuthorizationRequestRedirectFilter() {
        return new OAuth2AuthorizationRequestRedirectFilter(clientRegistrationRepository(), "/oauth2/authorization");
    }

    @Bean
    public OAuth2LoginAuthenticationFilter oAuth2LoginAuthenticationFilter() {
        OAuth2LoginAuthenticationFilter oAuth2LoginAuthenticationFilter = new OAuth2LoginAuthenticationFilter(clientRegistrationRepository(), new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository()));
        oAuth2LoginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        oAuth2LoginAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        oAuth2LoginAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        return oAuth2LoginAuthenticationFilter;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<String> clients = Arrays.asList("google", "kakao");
        List<ClientRegistration> registrations = clients.stream().map(this::getRegistration).filter(Objects::nonNull).collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");

        if (client.equals("google")) {
            ClientRegistration cr = CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId).redirectUri("https://hiavatar.minoflower.com/login/oauth2/code/google").clientSecret(clientSecret).scope("email", "profile").build();
            return  cr;
        }

        if (client.equals("kakao")) {
            return ClientRegistration.withRegistrationId("kakao")
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("https://hiavatar.minoflower.com/login/oauth2/code/kakao")
                    .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                    .tokenUri("https://kauth.kakao.com/oauth/token")
                    .userInfoUri("https://kapi.kakao.com/v2/user/me")
                    .scope("profile_nickname", "account_email")
                    .userNameAttributeName("id")
                    .build();
        }

        return null;
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository, socialLoginUserRepository);
    }

    @Bean
    public DefaultAuthorizationCodeTokenResponseClient defaultAuthorizationCodeTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }
}


