package com.fastcampus.finalproject.dto.request;

import lombok.Data;

@Data
public class GetAvatarPreviewRequest {

    private String avatarName;
    private String avatarType;
    private String bgName;
}