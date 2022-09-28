package com.fastcampus.finalproject.entity;

import com.fastcampus.finalproject.enums.LoginType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBasic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_UID")
    private Long uid;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;


    public UserBasic(String userName, LoginType loginType) {
        this.userName = userName;
        this.loginType = loginType;
    }

    public UserBasic(Long uid) {
        this.uid = uid;
    }
}