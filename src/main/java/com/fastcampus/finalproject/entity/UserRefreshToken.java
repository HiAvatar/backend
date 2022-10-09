package com.fastcampus.finalproject.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 86400)
public class UserRefreshToken {

    private Long uid;
    private String refreshToken;

    public UserRefreshToken(Long uid, String refreshToken) {
        this.uid = uid;
        this.refreshToken = refreshToken;
    }

    @Id
    public Long getUid() {
        return uid;
    }
}
