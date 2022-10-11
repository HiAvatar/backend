package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.config.WebClientConfig;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.fastcampus.finalproject.dto.AudioDto.AudioRequest;
import static com.fastcampus.finalproject.dto.AudioDto.AudioResponse;
import static com.fastcampus.finalproject.dto.VideoDto.VideoRequest;
import static com.fastcampus.finalproject.dto.VideoDto.VideoResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlaskCommunicationService {

    private final YmlFlaskConfig flaskConfig;
    private final WebClientConfig webClientConfig;

    public AudioResponse getAudioResult(AudioRequest request) {
        return webClientConfig.webClient()
                .post()
                .uri(flaskConfig.getRequestAudioApi())
                .bodyValue(request)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AudioResponse.class)
                .block();
    }

    public VideoResponse getVideoResult(VideoRequest request) {
        return webClientConfig.webClient()
                .post()
                .uri(flaskConfig.getRequestVideoApi())
                .bodyValue(request)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VideoResponse.class)
                .block();
    }
}