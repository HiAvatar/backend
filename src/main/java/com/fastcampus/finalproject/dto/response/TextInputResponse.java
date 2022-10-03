package com.fastcampus.finalproject.dto.response;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TextInputResponse { // Server -> Client
    private String result;
    //private List<Audios> audios;
    private String totalAudioUrl;

    public TextInputResponse(String result, String totalAudioUrl) {
        this.result = result;
        this.totalAudioUrl = totalAudioUrl;
    }

    public TextInputResponse() {

    }

//    @Data
//    public static class Audios {
//        private Integer audioId;
//        private String splitText;
//        private String audioFile;
//
//        public Audios(Integer audioId, String splitText, String audioFile) {
//            this.audioId = audioId;
//            this.splitText = splitText;
//            this.audioFile = audioFile;
//        }
//    }

}
