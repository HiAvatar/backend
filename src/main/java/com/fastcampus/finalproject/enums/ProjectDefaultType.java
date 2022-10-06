package com.fastcampus.finalproject.enums;

public enum ProjectDefaultType {

    EMPTY("미정"),
    LANGUAGE("한국어"),
    SEX("여자"),
    CHARACTER_VOICE("가영"),
    AVATAR_NAME("avatar1");

    private final String value;

    ProjectDefaultType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
