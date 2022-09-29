package com.fastcampus.finalproject.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

@Data
public class AudioFileUploadRequest {
    private String audioFileName;
    private Base64 texts;

    public AudioFileUploadRequest(String audioFileName, Base64 texts) {
        this.audioFileName = audioFileName;
        this.texts = texts;
    }
}
