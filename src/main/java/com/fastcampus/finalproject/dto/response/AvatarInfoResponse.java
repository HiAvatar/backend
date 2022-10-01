package com.fastcampus.finalproject.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvatarInfoResponse {
    private String result;
    private Long videoId;
    private String videoName;
//    private String thumbnail;
    private String videoUrl;
    private LocalDateTime createAt;

    public AvatarInfoResponse(String result, Long videoId, String videoName, /*String thumbnail,*/ String videoUrl, LocalDateTime createAt) {
        this.result = result;
        this.videoId = videoId;
        this.videoName = videoName;
        //this.thumbnail = thumbnail;
        this.videoUrl = videoUrl;
        this.createAt = createAt;
    }
}
