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

    //로그인을 위한 임시 생성자
    public UserBasic(String userName) {
        this.userName = userName;
        this.loginType = LoginType.NATIVE;
    }
}