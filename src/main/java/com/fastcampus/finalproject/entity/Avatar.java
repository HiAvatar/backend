package com.fastcampus.finalproject.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Avatar {

    @Column(name = "AVATAR_NAME")
    private String name;

    @Column(name = "AVATAR_TYPE")
    private String type;

    private String background;
}
