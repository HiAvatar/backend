package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.config.WebClientConfig;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.fastcampus.finalproject.dto.AudioDto.AudioRequest;
import static com.fastcampus.finalproject.dto.AudioDto.AudioResponse;
import static com.fastcampus.finalproject.dto.VideoDto.VideoRequest;
import static com.fastcampus.finalproject.dto.VideoDto.VideoResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlaskService {

    private final YmlFlaskConfig flaskConfig;
    private final WebClientConfig webClientConfig;

    public AudioResponse getAudioResult(AudioRequest request) {
        Mono<AudioResponse> response = webClientConfig.webClient()
                .post()
                .uri(flaskConfig.getRequestAudioApi())
                .bodyValue(request)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AudioResponse.class)
                .doOnNext(result -> log.info("flask 서버 요청중...(audioTexts:{})", request.getText()));

        return response.block();
    }

    public VideoResponse getVideoResult(VideoRequest request) {
        log.info("getVideoResult 실행");
        Mono<VideoResponse> response = webClientConfig.webClient()
                .post()
                .uri(flaskConfig.getRequestVideoApi())
                .bodyValue(request)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VideoResponse.class)
                .doOnNext(result -> log.info("flask 서버 요청중..."));
        log.info("getVideoResult 종료");
        return response.block();
    }

    public AudioRequest getAudioRequest(String texts) {
        return AudioRequest.builder()
                .text(texts)
                .narration("none")
                .path("result")
                .build();
    }

    public AudioResponse getAudioResponse(AudioRequest request) {
        return getAudioResult(request);
    }

    public VideoRequest getVideoRequest(String audioFileName, String avatarType, String bgName) {
        return VideoRequest.builder()
                .id(audioFileName)
                .avatar(avatarType)
                .background(bgName)
                .path("result")
                .build();
    }

    public VideoResponse getVideoResponse(VideoRequest request) {
        return getVideoResult(request);
    }
}