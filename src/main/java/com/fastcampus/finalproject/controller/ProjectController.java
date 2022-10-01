package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.request.*;
import com.fastcampus.finalproject.dto.response.*;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.service.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;

    private static final Long userUid = 1L;

    /**
     * 아바타 페이지 - 영상 합성
     * */
    @PostMapping("/projects/{projectId}/avatar")
    public ResponseWrapper<AvatarInfoResponse> addAvatarInfo(
            @PathVariable Long projectId,
            @RequestBody AvatarInfoRequest avatarInfoRequest) throws JsonProcessingException {
        return new ResponseWrapper<>(projectService.addAvatarInfo(projectId, avatarInfoRequest, userUid)).ok();
    }

    /**
     * 프로젝트 이름 변경
     * */
    @PatchMapping("/projects/{projectId}")
    public ResponseWrapper<UpdateProjectNameResponse> changeProjectName(
            @PathVariable Long projectId,
            @RequestBody UpdateProjectNameRequest projectName) {
        return new ResponseWrapper<>(projectService.changeProjectName(projectId, projectName)).ok();
    }

    /**
     * 로컬에서 업로드된 음성 파일을 DB에 저장
     * */
    @PostMapping("/projects/{projectId}/audio-file")
    public ResponseWrapper<Void> addAudioFile(
            @PathVariable("projectId") Long projectId,
            @RequestBody AudioFileUploadRequest audioFile) throws IOException {
        projectService.addAudioFile(projectId, audioFile);
        return new ResponseWrapper<Void>().ok();
    }

    /**
     * 텍스트 페이지에서 텍스트 하나 수정할 때 -> 음성 합성(File)
     * */
    @PostMapping("/projects/save/text")
    public ResponseWrapper<SentenceInputResponse> addSentence(
            @RequestBody SentenceInputRequest sentenceInputRequest) throws Exception {
        return new ResponseWrapper<>(projectService.getAudioFile(sentenceInputRequest)).ok();
    }

    /**
     * 텍스트 페이지에서 다음 버튼 또는 전체 듣기 버튼 누를 때 -> 음성 합성(URL)
     * */
    @PostMapping("/projects/{projectId}/save")
    public ResponseWrapper<TotalAudioSyntheticResponse> addTextPageAudioInfo(
            @PathVariable("projectId") Long projectId,
            @RequestBody TotalAudioSyntheticRequest totalAudioInfo) throws Exception {
        return new ResponseWrapper<>(projectService.addAudioInfo(projectId, totalAudioInfo)).ok();
    }

    /**
     * 텍스트 입력 팝업 창에서 완료 버튼 누를 때 -> 음성 합성(URL)
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
