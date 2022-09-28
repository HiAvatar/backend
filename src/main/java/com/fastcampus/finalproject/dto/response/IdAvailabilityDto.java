package com.fastcampus.finalproject.dto.response;

public class IdAvailabilityDto {
    private final boolean isIdAvailable;

    public IdAvailabilityDto(boolean isIdAvailable) {
        this.isIdAvailable = isIdAvailable;
    }

    public boolean isIdAvailable() {
        return isIdAvailable;
    }
}
