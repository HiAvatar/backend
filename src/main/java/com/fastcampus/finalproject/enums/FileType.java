package com.fastcampus.finalproject.enums;

public enum FileType {

    AUDIO("audio"),
    VIDEO("video");

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
