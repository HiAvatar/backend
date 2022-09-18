package com.fastcampus.finalproject.entity.dummy;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class DummyVoice {

    @Id
    private String characterVoice;

    @Column(name = "LANG")
    private String language;

    private String sex;

    private String previewAudio;
}
