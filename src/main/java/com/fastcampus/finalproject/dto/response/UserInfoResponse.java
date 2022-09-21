package com.fastcampus.finalproject.dto.response;

import lombok.Data;

@Data
public class UserInfoResponse {

    private String id;

    public UserInfoResponse(String id) {
        this.id = id;
    }
}
