package com.fastcampus.finalproject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SentenceInputResponse {

    private String result;
    private String audioFile;

    public SentenceInputResponse(String result) {
        this.result = result;
    }

    public SentenceInputResponse(String result, String audioFile) {
        this.result = result;
        this.audioFile = audioFile;
    }
}
