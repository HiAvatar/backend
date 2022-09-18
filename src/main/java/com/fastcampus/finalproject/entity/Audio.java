package com.fastcampus.finalproject.entity;

import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@Getter
public class Audio {

    @Column(name = "LANG")
    private String language;
    private String sex;
    private String characterVoice;

    @Column(columnDefinition = "TINYINT")
    private Integer speed;

    @Column(columnDefinition = "TINYINT")
    private Integer pitch;

    @Column(columnDefinition = "TINYINT")
    private Integer sentenceSpacing;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String texts;
}