package com.fastcampus.finalproject.dto;


import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.Video;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarDivision;
import com.fastcampus.finalproject.entity.dummy.DummyBackground;
import com.fastcampus.finalproject.util.CustomTimeUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

public class AvatarPageDto {

    @Data
    public static class AvatarPageRequest {

        private String avatarName;
        private String avatarType;
        private String bgName;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CompleteAvatarPageResponse {

        private String result;
        private Long videoId;
        private String videoName;
        private String videoUrl;
        private String createdAt;

        public CompleteAvatarPageResponse(String result) {
            this.result = result;
        }

        public CompleteAvatarPageResponse(String result, Video video) {
            this.result = result;
            this.videoId = video.getId();
            this.videoName = video.getName();
            this.videoUrl = video.getVideoUrl();
            this.createdAt = CustomTimeUtil.convertDateTime(video.getCreatedAt());
        }
    }

    @Data
    public static class GetAvatarPageResponse {

        private String avatarName;
        private String avatarType;
        private String bgName;
        private String language;
        private String sex;
        private String characterName;
        private Integer speed;
        private Integer pitch;
        private Integer sentenceSpacing;
        private AvatarPageDummyDto dummyData;

        public GetAvatarPageResponse(Project project, AvatarPageDummyDto dummyData) {
            this.avatarName = project.getAvatar().getName();
            this.avatarType = project.getAvatar().getType();
            this.bgName = project.getAvatar().getBackground();
            this.language = project.getAudio().getLanguage();
            this.sex = project.getAudio().getSex();
            this.characterName = project.getAudio().getCharacterVoice();
            this.speed = project.getAudio().getSpeed();
            this.pitch = project.getAudio().getPitch();
            this.sentenceSpacing = project.getAudio().getSentenceSpacing();
            this.dummyData = dummyData;
        }

        @Data
        public static class AvatarPageDummyDto {

            private AvatarSelectionDto avatar1;
            private AvatarSelectionDto avatar2;
            private AvatarSelectionDto avatar3;
            private AvatarSelectionDto avatar4;
            private List<BackgroundDto> backgroundList;

            public AvatarPageDummyDto(List<AvatarSelectionDto> avatarDtoList, List<BackgroundDto> backgroundDtoList) {
                this.avatar1 = avatarDtoList.get(0);
                this.avatar2 = avatarDtoList.get(1);
                this.avatar3 = avatarDtoList.get(2);
                this.avatar4 = avatarDtoList.get(3);
                this.backgroundList = backgroundDtoList;
            }
        }

        @Data
        public static class AvatarSelectionDto {
            String thumbnail;
            List<AvatarDivisionDto> detailList1;
            List<AvatarDivisionDto> detailList2;
            List<AvatarDivisionDto> detailList3;

            public AvatarSelectionDto(String thumbnail, List<AvatarDivisionDto> detailList1, List<AvatarDivisionDto> detailList2, List<AvatarDivisionDto> detailList3) {
                this.thumbnail = thumbnail;
                this.detailList1 = detailList1;
                this.detailList2 = detailList2;
                this.detailList3 = detailList3;
            }
        }

        @Data
        public static class AvatarDivisionDto {
            private String position;
            private String thumbnail;

            public AvatarDivisionDto(DummyAvatarDivision avatarDivision) {
                this.position = avatarDivision.getPosition();
                this.thumbnail = avatarDivision.getThumbnail();
            }
        }

        @Data
        public static class BackgroundDto {
            private String position;
            private String thumbnail;

            public BackgroundDto(DummyBackground background) {
                this.position = background.getBgName();
                this.thumbnail = background.getFileName();
            }
        }
    }

    @Data
    public static class AvatarPreviewResponse {

        private String thumbnail;

        public AvatarPreviewResponse(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}
