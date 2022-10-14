package com.fastcampus.finalproject.dto;

import lombok.Builder;
import lombok.Data;

public class AudioDto {

    @Data
    public static class AudioRequest {

        private String text;
        private String narration;
        private String path;

        @Builder
        public AudioRequest(String text, String narration, String path) {
            this.text = text;
            this.narration = narration;
            this.path = path;
        }
    }

    @Data
    public static class AudioResponse {

        private String id;
        private String status;
    }
}
