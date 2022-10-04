package com.fastcampus.finalproject.controller.advice;

import com.fastcampus.finalproject.controller.ProjectController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ProjectController.class)
public class ProjectControllerAdvice {

    //프로젝트 또는 영상이 5개인데 하나 더 생성하려 하면 에러 메시지 보낼 것
}
