package com.fastcampus.finalproject.dto.response;

import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarDivision;
import com.fastcampus.finalproject.entity.dummy.DummyBackground;
import lombok.Data;

import java.util.List;

@Data
public class GetAvatarPageResponse {

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

        private AvatarDto avatar1;
        private AvatarDto avatar2;
        private AvatarDto avatar3;
        private AvatarDto avatar4;
        private AvatarDto avatar5;
        private AvatarDto avatar6;
        private AvatarDto avatar7;
        private List<BackgroundDto> backgroundList;

        public AvatarPageDummyDto(List<AvatarDto> avatarDtoList, List<BackgroundDto> backgroundDtoList) {
            this.avatar1 = avatarDtoList.get(0);
            this.avatar2 = avatarDtoList.get(1);
            this.avatar3 = avatarDtoList.get(2);
            this.avatar4 = avatarDtoList.get(3);
            this.avatar5 = avatarDtoList.get(4);
            this.avatar6 = avatarDtoList.get(5);
            this.avatar7 = avatarDtoList.get(6);
            this.backgroundList = backgroundDtoList;
        }
    }

    @Data
    public static class AvatarDto {
        String thumbnail;
        List<AvatarDivisionDto> detailList1;
        List<AvatarDivisionDto> detailList2;
        List<AvatarDivisionDto> detailList3;

        public AvatarDto(String thumbnail, List<AvatarDivisionDto> detailList1, List<AvatarDivisionDto> detailList2, List<AvatarDivisionDto> detailList3) {
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