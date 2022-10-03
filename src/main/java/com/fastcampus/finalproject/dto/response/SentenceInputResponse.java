package com.fastcampus.finalproject.dto.response;

import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;


@Data
public class SentenceInputResponse {
    private String result;
    private String audioFile;

    public SentenceInputResponse(String result, String audioFile) {
        this.result = result;
        this.audioFile = audioFile;
    }
}
