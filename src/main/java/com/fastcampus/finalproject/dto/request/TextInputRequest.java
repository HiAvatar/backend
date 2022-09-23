package com.fastcampus.finalproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TextInputRequest { // Client -> Server
    private String texts;

    public TextInputRequest(String texts) {
        this.texts = texts;
    }

}
