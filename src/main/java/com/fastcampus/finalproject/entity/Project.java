package com.fastcampus.finalproject.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(name = "PROJECT_NAME", nullable = false)
    private String name;

    @Column(nullable = false)
    private String totalAudioUrl;

    @Column(nullable = false)
    private String audioFileName;

    @Embedded
    private Audio audio;

    @Embedded
    private Avatar avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_UID")
    private UserBasic user;
}