package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;

    private static final Long userUid = 1L;

    //init data
    @GetMapping("/login")
    public ResponseWrapper<Void> login() {
        userRepository.save(new UserBasic("userA"));
        return new ResponseWrapper<Void>()
                .ok();
    }
}
