package com.fastcampus.finalproject.dto.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class AvatarInfoRequest {
    private String avatarName;
    private String avatarType;
    private String bgName;

    public AvatarInfoRequest(String avatarName, String avatarType, String bgName) {
        this.avatarName = avatarName;
        this.avatarType = avatarType;
        this.bgName = bgName;
    }

    public String splitTotalAudioUrl(String totalAudioUrl) {
        // python API 에 영상 합성 보내기위해서, url 형식(https://../278962b8.wav)에서 순수 음성 파일 이름만(278962b8) 추출
        String audioNameExtension = totalAudioUrl.replace("https://../", "");
        log.info("audioNameExtension : {}", audioNameExtension);

        String audioName = audioNameExtension.replace(".wav", "");
        log.info("audioName : {}", audioName);
        return audioName;
    }

}
