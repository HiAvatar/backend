package com.fastcampus.finalproject.dto.response;

import lombok.Data;

@Data
public class CreateProjectResponse {

    private Long projectId;
    private String projectName;

    public CreateProjectResponse(Long projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }
}