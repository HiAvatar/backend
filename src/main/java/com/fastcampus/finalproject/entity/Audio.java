package com.fastcampus.finalproject.entity;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@Getter
public class Audio {

    @Column(name = "LANG", nullable = false)
    private String language;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private String characterVoice;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Integer speed;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Integer pitch;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Integer sentenceSpacing;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String texts;
}