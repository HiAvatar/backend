package com.fastcampus.finalproject.dto;

import com.fastcampus.finalproject.entity.Audio;
import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.fastcampus.finalproject.enums.ProjectDefaultType.EMPTY;

public class TextPageDto {

    /**
     * 텍스트 페이지에서 다음 버튼 또는 전체 듣기 버튼 누를 때  -> 음성 합성(URL)
     * /projects/{projectId}/save
     */
    /**
     * 텍스트 페이지 임시 저장
     * /projects/{projectId}/save
     */
    @Data
    @Slf4j
    public static class TotalAudioSyntheticRequest {
        private String texts;
        private String language;
        private String sex;
        private String characterName;
        private Integer speed;
        private Integer pitch;
        private Integer sentenceSpacing;
        private List<SplitText> splitTextList;

        @Data
        public static class SplitText {

            private Integer sentenceId;
            private String text;
            private Integer sentenceSpacing;
        }

        public String splitTextListToString() {
            // List<SplitText> 의 각 sentenceSpacing 들을 '|' 구분자로 나눠서 합친 후 -> 하나의 string 으로 반환해주기
            return this.splitTextList.stream().map(s -> s.getSentenceSpacing().toString()).collect(Collectors.joining("|"));
        }

        public void changeAudioInfo(Audio audio) {
            audio.changeAudioInfo(language, sex, characterName, speed, pitch, sentenceSpacing, texts, splitTextListToString());
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TotalAudioSyntheticResponse {

        private String result;
        private String totalAudioUrl;

        public TotalAudioSyntheticResponse(String result, String totalAudioUrl) {
            this.result = result;
            this.totalAudioUrl = totalAudioUrl;
        }
    }


    /**
     * 음성 업로드
     * /projects/{projectId}/audio-file
     */
    @Data
    @NoArgsConstructor
    public static class AudioFileUploadRequest {

        private String audioFileName;
        private String audioFile;
    }


    /**
     * 문장별 텍스트 음성 합성
     * /projects/save/text
     */
    @Data
    public static class SentenceInputRequest {

        private String text;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SentenceInputResponse {

        private String result;
        private String audioFile;

        public SentenceInputResponse(String result) {
            this.result = result;
        }

        public SentenceInputResponse(String result, String audioFile) {
            this.result = result;
            this.audioFile = audioFile;
        }
    }


    /**
     * 텍스트 입력 팝업창에서 음성 합성
     * /projects/{projectId}/save/script
     */
    @Data
    @NoArgsConstructor
    public static class TextInputRequest {

        private String texts;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TextInputResponse {

        private String result;
        private String totalAudioUrl;

        public TextInputResponse(String result) {
            this.result = result;
        }

        public TextInputResponse(String result, String totalAudioUrl) {
            this.result = result;
            this.totalAudioUrl = totalAudioUrl;
        }
    }


    /**
     * 텍스트 페이지 GET 요청
     * /projects/{projectId}/save
     */
    @Data
    public static class GetTextPageResponse {

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
            this.texts = getBlankIfInitProject(audio.getTexts());
            this.language = audio.getLanguage();
            this.sex = audio.getSex();
            this.characterName = audio.getCharacterVoice();
            this.speed = audio.getSpeed();
            this.pitch = audio.getPitch();
            this.sentenceSpacing = audio.getSentenceSpacing();
            this.splitTextList = getEmptyListIfInitProject(audio.getTexts(), splitTextList);
            this.totalAudioUrl = project.getTotalAudioUrl();
            this.dummyData = dummyData;
        }

        private String getBlankIfInitProject(String texts) {
            return isEmptyTexts(texts) ? "" : texts;
        }

        private List<TextDto> getEmptyListIfInitProject(String texts, List<TextDto> splitTextList) {
            return isEmptyTexts(texts) ? Collections.emptyList() : splitTextList;
        }

        private boolean isEmptyTexts(String texts) {
            return texts.equals(EMPTY.getValue());
        }

        @Data
        public static class TextDto {

            private Integer sentenceId;
            private String text;
            private Integer sentenceSpacing;

            public TextDto(Integer sentenceId, String text, Integer sentenceSpacing) {
                this.sentenceId = sentenceId;
                this.text = text;
                this.sentenceSpacing = sentenceSpacing;
            }
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
}
