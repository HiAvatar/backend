package com.fastcampus.finalproject.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static Long getCurrentUserUid() {
        Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authToken.getPrincipal();
    }

}
