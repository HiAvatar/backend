package com.fastcampus.finalproject.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 3600)
public class UserAccessToken {
    private Long uid;
    private String accessToken;

    public UserAccessToken(Long uid, String accessToken) {
        this.uid = uid;
        this.accessToken = accessToken;
    }

    @Id
    public Long getUid() {
        return this.uid;
    }

    public String getAccessToken() {
        return accessToken;
    }
}