package com.fastcampus.finalproject.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    private static final String EMPTY = "미정";

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

    public Project(UserBasic user) {
        this.name = EMPTY;
        this.totalAudioUrl = EMPTY;
        this.audioFileName = EMPTY;
        this.audio = new Audio();
        this.avatar = new Avatar();
        this.user = user;
    }

    public void initProjectName(String name) {
        this.name = name;
    }
}