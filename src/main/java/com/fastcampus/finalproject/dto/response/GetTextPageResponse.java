package com.fastcampus.finalproject.dto.response;

import com.fastcampus.finalproject.entity.Audio;
import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import lombok.Data;

import java.util.List;

@Data
public class GetTextPageResponse {

    private String texts;
    private String language;
    private String sex;
    private String characterName;
    private Integer speed;
    private Integer pitch;
    private Integer sentenceSpacing;
    private List<TextDto> splitTextList;
    private String totalAudioUrl;
    private TextPageDummyDto dummyData;

    public GetTextPageResponse(Project project, List<TextDto> splitTextList, TextPageDummyDto dummyData) {
        Audio audio = project.getAudio();
        this.texts = audio.getTexts();
        this.language = audio.getLanguage();
        this.sex = audio.getSex();
        this.characterName = audio.getCharacterVoice();
        this.speed = audio.getSpeed();
        this.pitch = audio.getPitch();
        this.sentenceSpacing = audio.getSentenceSpacing();
        this.splitTextList = splitTextList;
        this.totalAudioUrl = project.getTotalAudioUrl();
        this.dummyData = dummyData;
    }

    @Data
    public static class TextDto {

        private String text;
        private String audioUrl;
        private String sentenceSpacing;
    }

    @Data
    public static class TextPageDummyDto {

        private KoreanDto korean;
        private EnglishDto english;
        private ChineseDto chinese;

        public TextPageDummyDto(KoreanDto korean, EnglishDto english, ChineseDto chinese) {
            this.korean = korean;
            this.english = english;
            this.chinese = chinese;
        }
    }

    @Data
    public static class KoreanDto {

        private List<CharacterDto> femaleList;
        private List<CharacterDto> maleList;

        public KoreanDto(List<CharacterDto> femaleList, List<CharacterDto> maleList) {
            this.femaleList = femaleList;
            this.maleList = maleList;
        }
    }

    @Data
    public static class EnglishDto {

        private List<CharacterDto> femaleList;
        private List<CharacterDto> maleList;

        public EnglishDto(List<CharacterDto> femaleList, List<CharacterDto> maleList) {
            this.femaleList = femaleList;
            this.maleList = maleList;
        }
    }

    @Data
    public static class ChineseDto {

        private List<CharacterDto> femaleList;
        private List<CharacterDto> maleList;

        public ChineseDto(List<CharacterDto> femaleList, List<CharacterDto> maleList) {
            this.femaleList = femaleList;
            this.maleList = maleList;
        }
    }

    @Data
    public static class CharacterDto {

        private String characterName;
        private List<String> characterTags;
        private String audioUrl;

        public CharacterDto(DummyVoice dummyVoice) {
            this.characterName = dummyVoice.getCharacterVoice();
            this.characterTags = List.of(dummyVoice.getTags().split("\\|"));
            this.audioUrl = dummyVoice.getPreviewAudio();
        }
    }
}
