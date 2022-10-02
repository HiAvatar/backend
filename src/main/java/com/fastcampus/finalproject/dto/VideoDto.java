package com.fastcampus.finalproject.dto;

import lombok.Data;

public class VideoDto {

    @Data
    public static class VideoRequest {

        private String id; // audioId
        private String avatar; // avatarType
        private String background;
        private String path;

        public VideoRequest(String id, String avatar, String background, String path) {
            this.id = id;
            this.avatar = avatar;
            this.background = background;
            this.path = path;
        }
    }

    @Data
    public static class VideoResponse {

        private String status;
        private String id;
    }
}
