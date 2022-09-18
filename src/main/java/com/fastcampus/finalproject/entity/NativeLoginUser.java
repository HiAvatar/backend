package com.fastcampus.finalproject.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class NativeLoginUser {

    //변수명은 임시로 id로 한 것임
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_UID")
    private UserBasic user;
}