package com.fastcampus.finalproject.dto.request;

import com.fastcampus.finalproject.entity.Audio;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
    }

    public String splitTextListToString() {
        // List<SplitText> 의 각 sentenceSpacing 들을 '|' 구분자로 나눠서 합친 후 -> 하나의 string 으로 반환해주기
        return this.splitTextList.stream().map(s -> s.getSentenceSpacing().toString()).collect(Collectors.joining("|"));
    }

    public void changeAudioInfo(Audio audio) {
        audio.changeAudioInfo(language, sex, characterName, speed, pitch, sentenceSpacing, texts, splitTextListToString());
    }
}
