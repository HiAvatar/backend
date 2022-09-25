package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.dto.ResponseWrapper;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/test-api")
    public ResponseWrapper<Void> postTest(@RequestBody TestRequest request) {
        return new ResponseWrapper<Void>().ok();
    }

    @GetMapping("/test-api")
    public ResponseWrapper<TestResponse> getTest() {
        return new ResponseWrapper<>(new TestResponse("테스트가 성공했어용")).ok();
    }

    @Data
    static class TestRequest {

        private Long id;
        private String username;
    }

    @Data
    static class TestResponse {

        private String text;

        public TestResponse(String text) {
            this.text = text;
        }
    }
}
