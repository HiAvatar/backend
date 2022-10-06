package com.fastcampus.finalproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 *
 * @param <T> : Void로 설정할 경우, code, status만 json에 실림(@JsonInclude(JsonInclude.Include.NON_NULL) 기능 이용)
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T>{

    private T data;
    private int code;
    private String status;

    public ResponseWrapper(T data) {
        this.data = data;
    }

    public ResponseWrapper() {
    }

    public ResponseWrapper<T> ok() {
        this.code = OK.value();
        this.status = OK.getReasonPhrase();
        return this;
    }

    public ResponseWrapper<T> fail() {
        this.code = INTERNAL_SERVER_ERROR.value();
        this.status = INTERNAL_SERVER_ERROR.getReasonPhrase();
        return this;
    }

}
