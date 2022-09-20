package com.fastcampus.finalproject.enums;

public enum SexType {

    MALE("남자"),
    FEMALE("여자");

    private final String value;

    SexType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
