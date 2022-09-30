package com.fastcampus.finalproject.dto.response;

import lombok.Data;

@Data
public class GetAvatarPreviewResponse {

    private String thumbnail;

    public GetAvatarPreviewResponse(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}