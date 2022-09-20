package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.dto.response.CreateProjectResponse;
import com.fastcampus.finalproject.dto.response.GetHistoryResponse;
import com.fastcampus.finalproject.dto.response.GetTextPageResponse;
import com.fastcampus.finalproject.dto.response.GetTextPageResponse.EnglishDto;
import com.fastcampus.finalproject.dto.response.GetTextPageResponse.KoreanDto;
import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import com.fastcampus.finalproject.enums.SexType;
import com.fastcampus.finalproject.repository.DummyVoiceRepository;
import com.fastcampus.finalproject.repository.ProjectRepository;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.fastcampus.finalproject.dto.response.GetTextPageResponse.*;
import static com.fastcampus.finalproject.enums.LanguageType.*;
import static com.fastcampus.finalproject.enums.SexType.*;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final DummyVoiceRepository dummyVoiceRepository;

    @Transactional(readOnly = true)
    public GetHistoryResponse getHistory(Long userUid) {

        List<GetHistoryResponse.ProjectDto> projects = projectRepository.findAllByUserUid(userUid)
                .stream()
                .map(GetHistoryResponse.ProjectDto::new)
                .collect(Collectors.toList());

        List<GetHistoryResponse.VideoDto> videos = videoRepository.findAllByUserUid(userUid)
                .stream()
                .map(GetHistoryResponse.VideoDto::new)
                .collect(Collectors.toList());

        return new GetHistoryResponse(projects, videos);
    }

    @Transactional
    public CreateProjectResponse create(Long userUid) {
        UserBasic user = userRepository.findById(userUid).get();
        Project savedProject = projectRepository.save(new Project(user));

        savedProject.initProjectName(customizedDateTime(savedProject.getCreatedAt()));
        return new CreateProjectResponse(savedProject.getId(), savedProject.getName());
    }

    private String customizedDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

    @Transactional(readOnly = true)
    public GetTextPageResponse getTextPageData(Long userId, Long projectId) {

        Project findProject = projectRepository.findByUserUidAndId(userId, projectId)
                .orElseThrow(NoSuchElementException::new);

        //TODO api 내 api 호출로 해야 splitTextList 구현 가능
        List<TextDto> splitTextList = Collections.emptyList();

        //dummydata 만드는 함수
        List<DummyVoice> koreanList = dummyVoiceRepository.findAllByLanguage(KOREAN.getValue());
        List<DummyVoice> englishList = dummyVoiceRepository.findAllByLanguage(ENGLISH.getValue());
        List<DummyVoice> chineseList = dummyVoiceRepository.findAllByLanguage(CHINESE.getValue());

        KoreanDto korean = new KoreanDto(getGenderList(koreanList, FEMALE), getGenderList(koreanList, MALE));
        EnglishDto english = new EnglishDto(getGenderList(englishList, FEMALE), getGenderList(englishList, MALE));
        ChineseDto chinese = new ChineseDto(getGenderList(chineseList, FEMALE), getGenderList(chineseList, MALE));

        TextPageDummyDto dummyData = new TextPageDummyDto(korean, english, chinese);

        return new GetTextPageResponse(findProject, splitTextList, dummyData);
    }

    private List<CharacterDto> getGenderList(List<DummyVoice> list, SexType type) {
        return list.stream().filter(v -> v.getSex().equals(type.getValue()))
                .map(CharacterDto::new)
                .collect(Collectors.toList());
    }
}
