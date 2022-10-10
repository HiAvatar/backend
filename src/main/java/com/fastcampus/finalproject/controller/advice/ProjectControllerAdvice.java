package com.fastcampus.finalproject.controller.advice;

import com.fastcampus.finalproject.controller.ProjectController;
import com.fastcampus.finalproject.dto.ErrorResponseWrapper;
import com.fastcampus.finalproject.exception.NoCorrectProjectAccessException;
import com.fastcampus.finalproject.exception.NotSameSizeTwoListsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.io.FileNotFoundException;
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

    @ExceptionHandler
    public ErrorResponseWrapper notSameSizeTwoLists(NotSameSizeTwoListsException e) {
        return new ErrorResponseWrapper(null, "texts로부터 분리된 splitTextList 데이터가 올바르지 않습니다.")
                .badRequest();
    }

    /**
     * flask 서버와 통신하면서 발생하는 예외 처리
     */
    @ExceptionHandler
    public ErrorResponseWrapper flaskInternalError(RestClientException e) {
        return new ErrorResponseWrapper(e.getMessage(), "flask 서버의 내부 오류로 인해 요청이 실패했습니다.")
                .internalServerError();
    }

    /**
     * 특정 파일을 찾을 수 없을 때 발생하는 예외 처리
     * */
//    @ExceptionHandler
//    public ErrorResponseWrapper findAudioFileExHandle(FileNotFoundException e) {
//        return new ErrorResponseWrapper(e.getMessage(), "해당 경로에 파일이 존재하지 않습니다.")
//                .internalServerError();
//    }


}
