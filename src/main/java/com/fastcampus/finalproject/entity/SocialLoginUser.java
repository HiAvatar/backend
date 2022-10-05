package com.fastcampus.finalproject.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class SocialLoginUser {

    //변수명은 임시로 id로 한 것임
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthProvider;

    private LocalDateTime lastLoggedInDate;

    @OneToOne
    @JoinColumn(name = "USER_UID")
    private UserBasic user;

    public SocialLoginUser(String oauthProvider, LocalDateTime lastLoggedInDate, UserBasic user) {
        this.oauthProvider = oauthProvider;
        this.lastLoggedInDate = lastLoggedInDate;
        this.user = user;
    }
}