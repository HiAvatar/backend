package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.config.YmlFlaskConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static com.fastcampus.finalproject.dto.AudioDto.AudioRequest;
import static com.fastcampus.finalproject.dto.AudioDto.AudioResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlaskCommunicationService {

    private final ObjectMapper objectMapper;
    private final YmlFlaskConfig flaskConfig;

    public AudioResponse getAudioResult(AudioRequest request) {
        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        try {
            //Object Mapper로 JSON 바인딩
            String params = objectMapper.writeValueAsString(request);

            //HttpEntity에 헤더 및 params 설정
            HttpEntity<String> entity = new HttpEntity<>(params, httpHeaders);

            //RestTemplate의 exchange 메서드로 URL에 httpEntity와 함께 요청하기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    flaskConfig.getRequestAudioApi(),
                    HttpMethod.POST,
                    entity,
                    String.class);

            log.info("audio responseCode: {}", responseEntity.getStatusCode());
            log.info("audio responseBody: {}", responseEntity.getBody());

            return objectMapper.readValue(responseEntity.getBody(), AudioResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


