package com.fastcampus.finalproject.dto.request;

import lombok.Data;

import java.util.Dictionary;

@Data
public class AudioRequest { // Server -> Python
    private String text;
    private String narration;
    private String path;

    public AudioRequest(String text, String narration, String path) {
        this.text = text;
        this.narration = narration;
        this.path = path;
    }

    public AudioRequest() {

    }
}
