package com.fastcampus.finalproject.controller.advice;

import com.fastcampus.finalproject.controller.ProjectController;
import com.fastcampus.finalproject.dto.ErrorResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice(assignableTypes = ProjectController.class)
public class ProjectControllerAdvice {

    @ExceptionHandler
    public ErrorResponseWrapper findProjectExHandle(NoSuchElementException e) {
        return new ErrorResponseWrapper("프로젝트를 찾을 수 없습니다.", e.getMessage()).badRequest();
    }

}
