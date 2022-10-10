package com.fastcampus.finalproject.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    //Connection Pool을 사용하는 것과 같이 동작하기 위한 코드
    @Bean
    protected HttpClient httpClient() {
        return HttpClientBuilder.create()
                .setMaxConnTotal(50) // 최대로 연결을 유지할 커넥션 수
                .setMaxConnPerRoute(20) // IP, Port 한 쌍에 대해 수행할 커넥션 수
                .build();
    }

    @Bean
    protected HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(1000 * 5);
        factory.setReadTimeout(1000 * 60 * 5);
        factory.setHttpClient(httpClient);
        return factory;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(httpComponentsClientHttpRequestFactory(httpClient()));
    }
}
