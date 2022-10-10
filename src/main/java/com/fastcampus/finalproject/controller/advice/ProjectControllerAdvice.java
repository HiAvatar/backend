package com.fastcampus.finalproject.controller.advice;

import com.fastcampus.finalproject.controller.ProjectController;
import com.fastcampus.finalproject.dto.ErrorResponseWrapper;
import com.fastcampus.finalproject.exception.NoCorrectProjectAccessException;
import com.fastcampus.finalproject.exception.NoGetAudioFileBinaryException;
import com.fastcampus.finalproject.exception.NoCreateUploadAudioFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice(assignableTypes = ProjectController.class)
public class ProjectControllerAdvice {

    @ExceptionHandler
    public ErrorResponseWrapper findProjectExHandle(NoSuchElementException e) {
        return new ErrorResponseWrapper(null, "프로젝트를 찾을 수 없습니다.")
                .badRequest();
    }

    @ExceptionHandler
    public ErrorResponseWrapper validateCorrectUser(NoCorrectProjectAccessException e) {
        return new ErrorResponseWrapper(null, "사용자가 생성한 프로젝트 이외에는 접근할 수 없습니다.")
                .badRequest();
    }

    /**
     * flask 서버와 통신하면서 발생하는 예외 처리
     */
    @ExceptionHandler
    public ErrorResponseWrapper test(RestClientException e) {
        return new ErrorResponseWrapper(e.getMessage(), "flask 서버의 내부 오류로 인해 요청이 실패했습니다.")
                .internalServerError();
    }

    /**
     * 업로드된 음성 파일을 생성할 때 발생하는 예외 처리
     * */
    @ExceptionHandler
    public ErrorResponseWrapper saveAudioFileExHandle(NoCreateUploadAudioFileException e) {
        return new ErrorResponseWrapper(e.getMessage(), "업로드된 파일을 생성하지 못했습니다.")
                .internalServerError();
    }

    /**
     * 생성된 파일을 Binary 타입으로 변환할 때 생기는 예외 처리
     * */
    @ExceptionHandler
    public ErrorResponseWrapper getAudioFileBinaryExHandle(NoGetAudioFileBinaryException e) {
        return new ErrorResponseWrapper(e.getMessage(), "생성된 파일을 읽어들이지 못했습니다.")
                .internalServerError();
    }

}
