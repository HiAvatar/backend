package com.fastcampus.finalproject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextInputResponse {

    private String result;
    private String totalAudioUrl;

    public TextInputResponse(String result) {
        this.result = result;
    }

    public TextInputResponse(String result, String totalAudioUrl) {
        this.result = result;
        this.totalAudioUrl = totalAudioUrl;
    }
}