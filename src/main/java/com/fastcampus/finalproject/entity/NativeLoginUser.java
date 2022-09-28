package com.fastcampus.finalproject.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class NativeLoginUser {

    //변수명은 임시로 id로 한 것임
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private LocalDateTime lastUpdatedTime;

    @OneToOne
    @JoinColumn(name = "USER_UID")
    private UserBasic user;

    public NativeLoginUser() {
    }

    public NativeLoginUser(String password, UserBasic user) {
        this.password = password;
        this.user = user;
    }

    public NativeLoginUser(String password, LocalDateTime lastUpdatedTime, UserBasic user) {
        this.password = password;
        this.lastUpdatedTime = lastUpdatedTime;
        this.user = user;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeLastUpdatedTime(LocalDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}