package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.response.UserInfoResponse;
import com.fastcampus.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final Long userUid = 1L;

    @GetMapping("/auth")
    public ResponseWrapper<UserInfoResponse> getUserInfo() {
        return new ResponseWrapper<>(userService.getUserInfo(userUid))
                .ok();
    }
}
