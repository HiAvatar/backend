package com.fastcampus.finalproject.dto.response;

import lombok.Data;

@Data
public class UpdateProjectNameResponse {
    private String projectName;

    public UpdateProjectNameResponse(String projectName) {
        this.projectName = projectName;
    }
}
