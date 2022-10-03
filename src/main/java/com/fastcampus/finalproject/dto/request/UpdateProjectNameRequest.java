package com.fastcampus.finalproject.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProjectNameRequest {
    private String projectName;

    public UpdateProjectNameRequest(String projectName) {
        this.projectName = projectName;
    }
}
