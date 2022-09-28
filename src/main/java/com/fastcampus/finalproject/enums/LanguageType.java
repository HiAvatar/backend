package com.fastcampus.finalproject.enums;

public enum LanguageType {

    KOREAN("한국어"),
    ENGLISH("영어"),
    CHINESE("중국어");

    private final String value;

    LanguageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
