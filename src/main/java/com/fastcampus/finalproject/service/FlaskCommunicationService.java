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

import static com.fastcampus.finalproject.dto.AudioDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlaskCommunicationService {

    private final ObjectMapper objectMapper;
    private final YmlFlaskConfig flaskConfig;

    public AudioResponse getAudioResult(AudioRequest request) {
        try {
            HttpEntity<String> entity = getHttpEntity(request, getHttpHeaders());
            ResponseEntity<String> responseEntity = getResponseEntity(entity, flaskConfig.getRequestAudioApi());
            writeLogAboutResponse(responseEntity);

            return objectMapper.readValue(responseEntity.getBody(), AudioResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders getHttpHeaders() {
        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return httpHeaders;
    }

    private HttpEntity<String> getHttpEntity(AudioRequest request, HttpHeaders httpHeaders) throws JsonProcessingException {
        //Object Mapper로 JSON 바인딩
        String params = objectMapper.writeValueAsString(request);

        //HttpEntity에 헤더 및 params 설정
        return new HttpEntity<>(params, httpHeaders);
    }

    private ResponseEntity<String> getResponseEntity(HttpEntity<String> entity, String requestApi) {
        //RestTemplate의 exchange 메서드로 URL에 httpEntity와 함께 요청하기
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                requestApi,
                HttpMethod.POST,
                entity,
                String.class);
    }

    private void writeLogAboutResponse(ResponseEntity<String> responseEntity) {
        log.info("audio responseCode: {}", responseEntity.getStatusCode());
        log.info("audio responseBody: {}", responseEntity.getBody());
    }
}