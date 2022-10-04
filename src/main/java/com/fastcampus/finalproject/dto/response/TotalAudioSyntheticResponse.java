package com.fastcampus.finalproject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalAudioSyntheticResponse {

    private String result;
    private String totalAudioUrl;

    public TotalAudioSyntheticResponse(String result, String totalAudioUrl) {
        this.result = result;
        this.totalAudioUrl = totalAudioUrl;
    }
}