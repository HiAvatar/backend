package com.fastcampus.finalproject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "flask-communication")
@Getter
@Setter
public class YmlFlaskConfig {

    private String baseUrl;
    private String imagePath;
    private String filePath;
    private String underBar;
    private String imageExtension;
    private String audioExtension;
    private String videoExtension;
    private String requestAudioApi;
    private String requestVideoApi;

    public String createImageFilePath(String avatarType, String bgName) {
        return this.imagePath + avatarType + this.underBar + bgName + this.imageExtension;
    }

    public String createAudioFilePath(String fileName) {
        return this.filePath + fileName + this.audioExtension;
    }

    public String createVideoFilePath(String fileName) { return this.filePath + fileName + this.videoExtension; }
}
