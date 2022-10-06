package com.fastcampus.finalproject.controller.advice;

import com.fastcampus.finalproject.controller.ProjectController;
import com.fastcampus.finalproject.dto.ErrorResponseWrapper;
import com.fastcampus.finalproject.exception.NoCorrectProjectAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
