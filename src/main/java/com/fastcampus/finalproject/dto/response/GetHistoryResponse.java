package com.fastcampus.finalproject.dto.response;

import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.Video;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class GetHistoryResponse {

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
            this.lastModifiedAt = customizedDateTime(project.getLastModifiedAt());
        }

        private String customizedDateTime(LocalDateTime dateTime) {
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        }
    }

    @Data
    public static class VideoDto {

        private Long videoId;
        private String videoName;
        private String thumbnail;
        private String videoUrl;

        private String createdAt;

        public VideoDto(Video video) {
            this.videoId = video.getId();
            this.videoName = video.getName();
            //this.thumbnail = video.getThumbnail();
            this.videoUrl = video.getVideoUrl();
            this.createdAt = customizedDateTime(video.getCreatedAt());
        }
        private String customizedDateTime(LocalDateTime dateTime) {
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        }
    }
}
