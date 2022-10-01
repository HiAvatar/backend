package com.fastcampus.finalproject.dto.request;

import com.fastcampus.finalproject.entity.Audio;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
public class TotalAudioSyntheticRequest {
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

        public SplitText(Integer sentenceId, String text, Integer sentenceSpacing) {
            this.sentenceId = sentenceId;
            this.text = text;
            this.sentenceSpacing = sentenceSpacing;
        }
    }

    public TotalAudioSyntheticRequest(String texts, String language, String sex, String characterName, Integer speed, Integer pitch, Integer sentenceSpacing, List<SplitText> splitTextList) {
        this.texts = texts;
        this.language = language;
        this.sex = sex;
        this.characterName = characterName;
        this.speed = speed;
        this.pitch = pitch;
        this.sentenceSpacing = sentenceSpacing;
        this.splitTextList = splitTextList;
    }

    public String SplitTextListToEntity() {
        // List<SplitText> 의 각 sentenceSpacing 들을 '|' 구분자로 나눠서 합친 후 -> 하나의 string 으로 반환해주기
        String sentenceSpacing = this.splitTextList.stream().map(s -> s.getSentenceSpacing().toString()).collect(Collectors.joining("|"));
        log.info("toString : {}, toArray : {}, hasCode: {}", getSplitTextList().toString(), getSplitTextList().toArray(), getSplitTextList().hashCode());
        log.info(sentenceSpacing);
        return sentenceSpacing;
    }
}
