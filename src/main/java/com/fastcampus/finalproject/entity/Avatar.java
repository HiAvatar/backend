package com.fastcampus.finalproject.entity;

import com.fastcampus.finalproject.dto.request.AvatarInfoRequest;
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

    public void changeAvatarInfo(AvatarInfoRequest request) {
        this.name = request.getAvatarName();
        this.type = request.getAvatarType();
        this.background = request.getBgName();
    }
}
