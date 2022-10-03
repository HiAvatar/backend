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

    private String path;
    private String underBar;
    private String extension;

    public String createFilePath(String avatarType, String bgName) {
        return this.path + avatarType + this.underBar + bgName + this.extension;
    }
}
