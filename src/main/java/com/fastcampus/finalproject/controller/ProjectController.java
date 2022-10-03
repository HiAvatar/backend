package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.dto.AvatarPageDto.*;
import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.request.InsertTextPageRequest;
import com.fastcampus.finalproject.dto.response.*;
import com.fastcampus.finalproject.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseWrapper<>(projectService.getTextPageData(projectId))
                .ok();
    }

    @GetMapping("/projects/{projectId}/avatar")
    public ResponseWrapper<GetAvatarPageResponse> getAvatarSelectionPage(@PathVariable Long projectId) {
        return new ResponseWrapper<>(projectService.getAvatarPageData(projectId))
                .ok();
    }

    @PostMapping("/projects/avatar-preview")
    public ResponseWrapper<AvatarPreviewResponse> getAvatarPreview(@RequestBody AvatarPageRequest request) {
        return new ResponseWrapper<>(projectService.getAvatarPreview(request))
                .ok();
    }

    @PostMapping("/projects/{projectId}/save")
    public ResponseWrapper<InsertTextPageResponse> insertTextPageTemp(@PathVariable Long projectId, @RequestBody InsertTextPageRequest request) {
        return new ResponseWrapper<>(projectService.insertTextPageTemp(projectId, request))
                .ok();
    }

    @PostMapping("/projects/{projectId}/avatar")
    public ResponseWrapper<CompleteAvatarPageResponse> insertAvatarPageTemp(@PathVariable Long projectId, @RequestBody AvatarPageRequest request) {
        return new ResponseWrapper<>(projectService.insertAvatarPageTemp(projectId, request))
                .ok();
    }
}