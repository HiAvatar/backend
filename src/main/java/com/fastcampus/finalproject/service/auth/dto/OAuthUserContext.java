package com.fastcampus.finalproject.service.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class OAuthUserContext extends DefaultOAuth2User {

    private final long userUid;

    public OAuthUserContext(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, long userUid) {
        super(authorities, attributes, nameAttributeKey);
        this.userUid = userUid;
    }

    public long getUserUid() {
        return userUid;
    }
}
