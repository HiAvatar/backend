package com.fastcampus.finalproject.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.fastcampus.finalproject.enums.ProjectDefaultType.*;

@Embeddable
@Getter
public class Avatar {

    @Column(name = "AVATAR_NAME")
    private String name;

    @Column(name = "AVATAR_TYPE")
    private String type;

    private String background;

    public Avatar() {
        this.name = AVATAR_NAME.getValue();
        this.type = EMPTY.getValue();
        this.background = EMPTY.getValue();
    }

    public void changeAvatarInfo(String name, String type, String background) {
        this.name = name;
        this.type = type;
        this.background = background;
    }

    public void changeAvatarName(String avatarName) {
        this.name = avatarName;
    }

    public void changeTempAvatarTypeNotNull(String avatarType) {
        this.type = avatarType;
    }

    public void changeTempBgNameNotNull(String bgName) {
        this.background = bgName;
    }
}
