package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.dto.AvatarPageDto.AvatarPageRequest;
import com.fastcampus.finalproject.dto.AvatarPageDto.AvatarPreviewResponse;
import com.fastcampus.finalproject.dto.AvatarPageDto.CompleteAvatarPageResponse;
import com.fastcampus.finalproject.dto.AvatarPageDto.GetAvatarPageResponse;
import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.request.*;
import com.fastcampus.finalproject.dto.response.*;
import com.fastcampus.finalproject.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
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

    /**
     * 프로젝트 이름 변경
     * */
    @PatchMapping("/projects/{projectId}")
    public ResponseWrapper<UpdateProjectNameResponse> changeProjectName(
            @PathVariable Long projectId,
            @RequestBody UpdateProjectNameRequest request) {
        return new ResponseWrapper<>(projectService.changeProjectName(projectId, request))
                .ok();
    }

    /**
     * 텍스트 페이지에서 다음 버튼 또는 전체 듣기 버튼 누를 때 -> 음성 합성(URL)
     * */
    @PostMapping("/projects/{projectId}/save")
    public ResponseWrapper<TotalAudioSyntheticResponse> addTextPageAudioInfo(@PathVariable Long projectId, @RequestBody TotalAudioSyntheticRequest request) {
        return new ResponseWrapper<>(projectService.addAudioInfo(projectId, request))
                .ok();
    }

    /**
     * 로컬에서 업로드된 음성 파일을 DB에 저장
     * */
    @PostMapping("/projects/{projectId}/audio-file")
    public ResponseWrapper<Void> addAudioFile(@PathVariable Long projectId, @RequestBody AudioFileUploadRequest request) {
        projectService.uploadAudioFile(projectId, request);
        return new ResponseWrapper<Void>()
                .ok();
    }

    /**
     * 텍스트 페이지에서 텍스트 하나 수정할 때 -> 음성 합성(File)
     * */
    @PostMapping("/projects/save/text")
    public ResponseWrapper<SentenceInputResponse> addSentence(@RequestBody SentenceInputRequest request) {
        return new ResponseWrapper<>(projectService.getAudioFileAboutOneSentence(request))
                .ok();
    }

    /**
     * 텍스트 입력 팝업 창에서 완료 버튼 누를 때 -> 음성 합성(URL)
     * */
    @PostMapping("/projects/{projectId}/save/script")
    public ResponseWrapper<TextInputResponse> addTexts(@PathVariable Long projectId, @RequestBody TextInputRequest request) {
        TextInputResponse textInputResponse = projectService.getAudioFileAboutScript(projectId, request);
        return new ResponseWrapper<>(textInputResponse)
                .ok();
    }

    /**
     * 아바타 페이지 - 영상 합성
     * */
    @PostMapping("/projects/{projectId}/avatar")
    public ResponseWrapper<CompleteAvatarPageResponse> addAvatarInfo(@PathVariable Long projectId, @RequestBody AvatarPageRequest request) {
        return new ResponseWrapper<>(projectService.addAvatarInfo(projectId, request))
                .ok();
    }

    /**
     * 텍스트 페이지 임시 저장
     * */
    @PatchMapping("/projects/{projectId}/save")
    public ResponseWrapper<Void> saveTextPageAudioInfo(@PathVariable Long projectId, @RequestBody TotalAudioSyntheticRequest request) {
        projectService.saveAudioInfo(projectId, request);
        return new ResponseWrapper<Void>()
                .ok();
    }

    /**
     * 아바타 페이지 임시 저장
     * */
    @PatchMapping("/projects/{projectId}/avatar")
    public ResponseWrapper<Void> addTempAvatarInfo(@PathVariable Long projectId, @RequestBody AvatarPageRequest request) {
        projectService.addTempAvatarInfo(projectId, request);
        return new ResponseWrapper<Void>()
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
}
