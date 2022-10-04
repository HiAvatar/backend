package com.fastcampus.finalproject.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AudioFileUploadRequest {

    private String audioFileName;
    private String audioFile;
}
