package com.fastcampus.finalproject.service.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String oauthProvider;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String oauthProvider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = nameAttributeKey;
        this.email = email;
        this.oauthProvider = oauthProvider;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        OAuthAttributes oAuthAttributes = null;

        switch (registrationId) {
            case "google":
                oAuthAttributes = ofGoogle(userNameAttributeName, attributes);
                break;
            case "kakao":
                oAuthAttributes = ofKakao(userNameAttributeName, attributes);
                break;
            default:
                break;
        }

        return oAuthAttributes;
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .oauthProvider("Google")
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        String nickname = (String) ((LinkedHashMap) attributes.get("properties")).get("nickname");
        String email = (String) ((LinkedHashMap) attributes.get("kakao_account")).get("email");

        return OAuthAttributes.builder()
                .name(nickname)
                .email(email)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .oauthProvider("Kakao")
                .build();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
