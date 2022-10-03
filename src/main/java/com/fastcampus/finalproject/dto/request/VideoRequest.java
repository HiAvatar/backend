package com.fastcampus.finalproject.dto.request;

import lombok.Data;

@Data
public class VideoRequest { // Server -> Python
    private String id;          // 오디오 아이디
    private String avatar;      // 아바타 옵션 값
    private String background;  // 배경
    private String path;        // 저장 경로

    public VideoRequest(String id, String avatar, String background, String path) {
        this.id = id;
        this.avatar = avatar;
        this.background = background;
        this.path = path;
    }
}
