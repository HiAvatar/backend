package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.request.TextInputRequest;
import com.fastcampus.finalproject.dto.response.*;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;

    private static final Long userUid = 1L;

    /**
     * 텍스트 입력 팝업 창에서 완료 버튼 누를 때
     * */
    @PostMapping("/projects/{projectId}/save/script")
    public ResponseWrapper<TextInputResponse> addTexts(
            @PathVariable("projectId") Long projectId,
            @RequestBody TextInputRequest texts) throws Exception {
        log.info("texts : {}", texts.getTexts());
        TextInputResponse textInputResponse = projectService.getAudio(projectId, texts);
        return new ResponseWrapper<>(textInputResponse).ok();
    }

    //init data
    @GetMapping("/login-test")
    public ResponseWrapper<Void> login() {
        userRepository.save(new UserBasic("userA"));
        return new ResponseWrapper<Void>()
                .ok();
    }

    @GetMapping("/projects")
    public ResponseWrapper<GetHistoryResponse> getHistory() {
        return new ResponseWrapper<>(projectService.getHistory(userUid))
                .ok();
    }

    @PostMapping("/projects")
    public ResponseWrapper<CreateProjectResponse> createProject() {
        return new ResponseWrapper<>(projectService.create(userUid))
                .ok();
    }

    @GetMapping("/projects/{projectId}/save")
    public ResponseWrapper<GetTextPageResponse> getTextPage(@PathVariable Long projectId) {
        return new ResponseWrapper<>(projectService.getTextPageData(1L, projectId))
                .ok();
    }

    @GetMapping("/projects/{projectId}/avatar")
    public ResponseWrapper<GetAvatarPageResponse> getAvatarSelectionPage(@PathVariable Long projectId) {
        return new ResponseWrapper<>(projectService.getAvatarPageData(1L, projectId)).ok();
    }

}
