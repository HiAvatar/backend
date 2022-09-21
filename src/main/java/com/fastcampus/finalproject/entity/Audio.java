package com.fastcampus.finalproject.entity;

import com.fastcampus.finalproject.enums.ProjectDefaultType;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import static com.fastcampus.finalproject.enums.ProjectDefaultType.*;

@Embeddable
@Getter
public class Audio {

    private static final Integer DEFAULT_CONTROL = 0;

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

    public Audio() {
        this.language = LANGUAGE.getValue();
        this.sex = SEX.getValue();
        this.characterVoice = CHARACTER_VOICE.getValue();
        this.speed = DEFAULT_CONTROL;
        this.pitch = DEFAULT_CONTROL;
        this.sentenceSpacing = DEFAULT_CONTROL;
        this.texts = EMPTY.getValue();
    }
}