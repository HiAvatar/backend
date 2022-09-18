package com.fastcampus.finalproject.entity;

import com.fastcampus.finalproject.enums.LoginType;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserBasic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_UID")
    private Long uid;

    private String userName;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;
}