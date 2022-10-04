package com.fastcampus.finalproject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsertTextPageResponse {

    private String result;
    private String totalAudioUrl;

    public InsertTextPageResponse(String result, String totalAudioUrl) {
        this.result = result;
        this.totalAudioUrl = totalAudioUrl;
    }
}
