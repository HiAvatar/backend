package com.fastcampus.finalproject.enums;

public enum FlaskResponseType {

    SUCCESS("Success"),
    FAILED("Failed");

    private final String value;

    FlaskResponseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
