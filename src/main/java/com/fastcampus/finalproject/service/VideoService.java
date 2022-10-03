package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.dto.request.VideoRequest;
import com.fastcampus.finalproject.dto.response.AudioResponse;
import com.fastcampus.finalproject.dto.response.VideoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
    /** 비디오 생성
     *  application server -> python -> application server
     * */
    @Transactional
    public VideoResponse getVideo(VideoRequest videoRequest) throws JsonProcessingException {
        // 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        // Object to String
        ObjectMapper objectMapper = new ObjectMapper();
        String videoRequestParam = objectMapper.writeValueAsString(videoRequest);
        log.info("videoRequest : {}", videoRequest);
        log.info("videoRequestParam : {}", videoRequestParam);

        // HttpEntity에 헤더 및 Params 설정
        HttpEntity entity = new HttpEntity(videoRequestParam, httpHeaders);
        log.info("entity : {}", entity.getBody());

        // RestTemplate 의 exchange 메서드를 통해 URL 에 HttpEntity 와 함께 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8000/request_video", HttpMethod.POST, entity, String.class);

        // 요청 후 응답 확인
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());

        // String to Object
        ObjectMapper objectMapper2 = new ObjectMapper();
        VideoResponse response = objectMapper2.readValue(responseEntity.getBody(), VideoResponse.class);
        return response;
    }


}
