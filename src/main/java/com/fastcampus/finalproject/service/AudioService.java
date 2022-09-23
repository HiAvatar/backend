package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.dto.request.AudioRequest;
import com.fastcampus.finalproject.dto.response.AudioResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class AudioService {

    /** 오디오 생성
     *  application server -> python -> application server
     * */
    @Transactional
    public AudioResponse getAudio(AudioRequest audioRequest) throws Exception{
        // 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
/*
        // audioRequest 에 texts 를 get 해서 split -> 각 split text 마다 /test api 로 요청
        //List<String> list = new ArrayList<>();
        String[] splitTexts = audioRequest.getText().split("\\.");

        // postService에서 Split해서 옮기기
        for (String s : splitTexts) {
            audioRequest.setText(s);
            //audioRequest.setText("안녕하세요. 반갑습니다.");
            audioRequest.setNarration("");
            audioRequest.setPath("result");

        }

*/
        // Object to String
        ObjectMapper objectMapper = new ObjectMapper();
        String audioRequestParam = objectMapper.writeValueAsString(audioRequest);

        // HttpEntity에 헤더 및 Params 설정
        HttpEntity entity = new HttpEntity(audioRequestParam, httpHeaders);

        // RestTemplate 의 exchange 메서드를 통해 URL 에 HttpEntity 와 함께 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8000/request_audio", HttpMethod.POST, entity, String.class);

        // 요청 후 응답 확인
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());

        // String to Object
        ObjectMapper objectMapper2 = new ObjectMapper();
        AudioResponse response = objectMapper2.readValue(responseEntity.getBody(), AudioResponse.class);
        return response;
    }
}
