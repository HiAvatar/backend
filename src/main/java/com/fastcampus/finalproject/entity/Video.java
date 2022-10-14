package com.fastcampus.finalproject.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Video(String name, String videoFileName, String videoUrl, UserBasic user) {
        this.name = name;
        this.videoFileName = videoFileName;
        this.videoUrl = videoUrl;
        this.user = user;
    }
}