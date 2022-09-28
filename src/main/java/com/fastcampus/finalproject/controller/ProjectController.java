package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.response.CreateProjectResponse;
import com.fastcampus.finalproject.dto.response.GetAvatarPageResponse;
import com.fastcampus.finalproject.dto.response.GetHistoryResponse;
import com.fastcampus.finalproject.dto.response.GetTextPageResponse;
import com.fastcampus.finalproject.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/projects")
    public ResponseWrapper<GetHistoryResponse> getHistory() {
        return new ResponseWrapper<>(projectService.getHistory(AuthUtil.getCurrentUserUid()))
                .ok();
    }

    @PostMapping("/projects")
    public ResponseWrapper<CreateProjectResponse> createProject() {
        return new ResponseWrapper<>(projectService.create(AuthUtil.getCurrentUserUid()))
                .ok();
    }

    @GetMapping("/projects/{projectId}/save")
    public ResponseWrapper<GetTextPageResponse> getTextPage(@PathVariable Long projectId) {
        return new ResponseWrapper<>(projectService.getTextPageData(AuthUtil.getCurrentUserUid(), projectId))
                .ok();
    }

    @GetMapping("/projects/{projectId}/avatar")
    public ResponseWrapper<GetAvatarPageResponse> getAvatarSelectionPage(@PathVariable Long projectId) {
        return new ResponseWrapper<>(projectService.getAvatarPageData(AuthUtil.getCurrentUserUid(), projectId)).ok();
    }
}