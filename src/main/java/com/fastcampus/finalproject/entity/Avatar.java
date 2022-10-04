package com.fastcampus.finalproject.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.fastcampus.finalproject.enums.ProjectDefaultType.BACKGROUND;
import static com.fastcampus.finalproject.enums.ProjectDefaultType.EMPTY;

@Embeddable
@Getter
public class Avatar {

    @Column(name = "AVATAR_NAME")
    private String name;

    @Column(name = "AVATAR_TYPE")
    private String type;

    private String background;

    public Avatar() {
        this.name = EMPTY.getValue();
        this.type = EMPTY.getValue();
        this.background = BACKGROUND.getValue();
    }

    public void changeAvatarInfo(String name, String type, String background) {
        this.name = name;
        this.type = type;
        this.background = background;
    }
}
