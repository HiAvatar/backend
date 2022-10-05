package com.fastcampus.finalproject.service.auth;

import com.fastcampus.finalproject.entity.SocialLoginUser;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.enums.LoginType;
import com.fastcampus.finalproject.repository.SocialLoginUserRepository;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.service.auth.dto.OAuthAttributes;
import com.fastcampus.finalproject.service.auth.dto.OAuthUserContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final SocialLoginUserRepository socialLoginUserRepository;

    public CustomOAuth2UserService(UserRepository userRepository, SocialLoginUserRepository socialLoginUserRepository) {
        this.userRepository = userRepository;
        this.socialLoginUserRepository = socialLoginUserRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes authAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        long uid = saveOrUpdate(authAttributes);

        return new OAuthUserContext(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), authAttributes.getAttributes(), authAttributes.getNameAttributeKey(), uid);
    }

    private long saveOrUpdate(OAuthAttributes attributes) {
        String userId = attributes.getEmail();
        Optional<UserBasic> userBasicOptional = userRepository.findByUserNameAndLoginType(attributes.getEmail(), LoginType.SOCIAL);
        if (userBasicOptional.isEmpty()) {
            UserBasic signedUpSocialUser = userRepository.save(new UserBasic(userId, LoginType.SOCIAL));
            socialLoginUserRepository.save(new SocialLoginUser(attributes.getOauthProvider(), LocalDateTime.now(), signedUpSocialUser));
            return userRepository.findByUserNameAndLoginType(attributes.getEmail(), LoginType.SOCIAL).get().getUid();
        }
        return userBasicOptional.get().getUid();
    }
}
