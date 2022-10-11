package com.fastcampus.finalproject.dto;

import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.Video;
import com.fastcampus.finalproject.util.CustomTimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProjectHistoryDto {

    /**
     * 히스토리 조회
     * /projects
     */
    @Data
    public static class GetHistoryResponse {

        private List<ProjectDto> projects;
        private List<VideoDto> videos;

        public GetHistoryResponse(List<ProjectDto> projects, List<VideoDto> videos) {
            this.projects = projects;
            this.videos = videos;
        }

        @Data
        public static class ProjectDto {

            private Long projectId;
            private String projectName;
            private String lastModifiedAt;

            public ProjectDto(Project project) {
                this.projectId = project.getId();
                this.projectName = project.getName();
                this.lastModifiedAt = CustomTimeUtil.convertDateTime(project.getLastModifiedAt());
            }
        }

        @Data
        public static class VideoDto {

            private Long videoId;
            private String videoName;
            private String videoUrl;
            private String createdAt;

            public VideoDto(Video video) {
                this.videoId = video.getId();
                this.videoName = video.getName();
                this.videoUrl = video.getVideoUrl();
                this.createdAt = CustomTimeUtil.convertDateTime(video.getCreatedAt());
            }
        }
    }


    /**
     * 프로젝트 생성
     * /projects
     */
    @Data
    public static class CreateProjectResponse {

        private Long projectId;
        private String projectName;

        public CreateProjectResponse(Long projectId, String projectName) {
            this.projectId = projectId;
            this.projectName = projectName;
        }
    }


    /**
     * 프로젝트 이름 변경
     * /projects/{projectId}
     */
    @Data
    @NoArgsConstructor
    public static class UpdateProjectNameRequest {

        private String projectName;
    }

    @Data
    public static class UpdateProjectNameResponse {
        private String projectName;

        public UpdateProjectNameResponse(String projectName) {
            this.projectName = projectName;
        }
    }
}
