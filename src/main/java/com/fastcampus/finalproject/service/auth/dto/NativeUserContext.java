package com.fastcampus.finalproject.service.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class NativeUserContext extends User {
    private final Long uid;

    public NativeUserContext(Long uid, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.uid = uid;
    }

    public Long getUid() {
        return uid;
    }
}
