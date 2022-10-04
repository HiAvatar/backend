package com.fastcampus.finalproject.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SentenceInputRequest {
    private String texts;

    public SentenceInputRequest(String texts) {
        this.texts = texts;
    }
}
