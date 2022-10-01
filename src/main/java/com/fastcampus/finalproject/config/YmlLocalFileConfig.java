package com.fastcampus.finalproject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "local-file")
@Getter
@Setter
public class YmlLocalFileConfig {

    private String imagePath;
    private String filePath;
    private String underBar;
    private String imageExtension;
    private String audioExtension;
    private String videoExtension;

    public String createImageFilePath(String avatarType, String bgName) {
        return this.imagePath + avatarType + this.underBar + bgName + this.imageExtension;
    }
}
