package com.fastcampus.finalproject.dto.response;

import lombok.Data;

@Data
public class AudioResponse { // Python -> Server
    private String status;
    private String id;

    public AudioResponse(String status, String id) {
        this.status = status;
        this.id = id;
    }

    public AudioResponse() {

    }


}
