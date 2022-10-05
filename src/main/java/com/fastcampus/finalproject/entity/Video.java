package com.fastcampus.finalproject.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VIDEO_ID")
    private Long id;

    @Column(name = "VIDEO_NAME", nullable = false)
    private String name;

    @Column(nullable = false)
    private String videoFileName;

    @Column(nullable = false)
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_UID")
    private UserBasic user;

    public Video(String name, String videoFileName, String videoUrl, UserBasic user) {
        this.name = name;
        this.videoFileName = videoFileName;
        this.videoUrl = videoUrl;
        this.user = user;
    }

    public Video() {}

}