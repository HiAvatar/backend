package com.fastcampus.finalproject.dto.response;

import lombok.Data;

import javax.websocket.Decoder;
import java.util.List;

@Data
public class TotalAudioSyntheticResponse {
    private String result;
    private String totalAudioUrl;

    public TotalAudioSyntheticResponse(String result, String totalAudioUrl) {
        this.result = result;
        this.totalAudioUrl = totalAudioUrl;
    }
}
