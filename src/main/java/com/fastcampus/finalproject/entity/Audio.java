package com.fastcampus.finalproject.entity;

import com.fastcampus.finalproject.dto.request.TotalAudioSyntheticRequest;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;


import static com.fastcampus.finalproject.enums.ProjectDefaultType.*;

@Embeddable
@Getter
public class Audio {

    private static final Integer DEFAULT_CONTROL = 0;
    //private static final String DEFAULT_SENTENCE_SPACING_LIST = "0";

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

    @Column(nullable = false)
    private String sentenceSpacingList;

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
        this.sentenceSpacingList = EMPTY.getValue();
        this.texts = EMPTY.getValue();
        //this.sentenceSpacingList = DEFAULT_SENTENCE_SPACING_LIST;
    }

    public void changeAudioInfo(TotalAudioSyntheticRequest totalAudioInfo) {
        this.language = totalAudioInfo.getLanguage();
        this.sex = totalAudioInfo.getSex();
        this.characterVoice = totalAudioInfo.getCharacterName();
        this.speed = totalAudioInfo.getSpeed();
        this.pitch = totalAudioInfo.getPitch();
        this.sentenceSpacing = totalAudioInfo.getSentenceSpacing();
        this.texts = totalAudioInfo.getTexts();
        this.sentenceSpacingList = totalAudioInfo.SplitTextListToEntity();
    }

    public void changeTexts(String texts) {
        this.texts = texts;
    }
}