package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.dto.request.AudioRequest;
import com.fastcampus.finalproject.dto.request.TextInputRequest;
import com.fastcampus.finalproject.dto.response.*;
import com.fastcampus.finalproject.dto.response.GetAvatarPageResponse.AvatarDivisionDto;
import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarDivision;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarList;
import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import com.fastcampus.finalproject.enums.SexType;
import com.fastcampus.finalproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.fastcampus.finalproject.dto.response.GetAvatarPageResponse.*;
import static com.fastcampus.finalproject.dto.response.GetAvatarPageResponse.AvatarDto;
import static com.fastcampus.finalproject.dto.response.GetAvatarPageResponse.AvatarPageDummyDto;
import static com.fastcampus.finalproject.dto.response.GetTextPageResponse.*;
import static com.fastcampus.finalproject.enums.LanguageType.*;
import static com.fastcampus.finalproject.enums.SexType.FEMALE;
import static com.fastcampus.finalproject.enums.SexType.MALE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final DummyVoiceRepository dummyVoiceRepository;
    private final DummyAvatarListRepository dummyAvatarListRepository;
    private final DummyAvatarDivisionRepository dummyAvatarDivisionRepository;
    private final DummyBackgroundRepository dummyBackgroundRepository;
    private final AudioService audioService;

    private static final int START_AVATAR_IDX = 1;
    private static final int END_AVATAR_IDX = 7;


    /**
     * 처음 텍스트 팝업창으로 저장
     * */
    @Transactional
    public TextInputResponse getAudio(Long projectId, TextInputRequest texts) throws Exception{
        Project findProject = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("프로젝트가 존재하지 않습니다."));

        AudioResponse audioResponse = new AudioResponse();
        audioResponse = audioService.getAudio(new AudioRequest(texts.getTexts(), "", "result"));

        TextInputResponse textInputResponse = new TextInputResponse();
        textInputResponse.setResult(audioResponse.getStatus());
        textInputResponse.setTotalAudioUrl(audioResponse.getId());
        findProject.changeTotalAudioURl(audioResponse.getId());
        return textInputResponse;

/*
        // Java -> Python 으로  HTTP 요청
//        AudioRequest audioRequest = new AudioRequest();
//        log.info("texts.getTexts() : {}", texts.getTexts());
//        audioRequest.setText(texts.getTexts());
//        audioRequest.setPath("result");
//        audioRequest.setNarration("");


        // audioRequest 에 texts 를 get 해서 split -> 각 split text 마다 python api 로 요청
        //String[] splitTexts = audioRequest.getText().split("\\.");
        List<String> splitTextList = Arrays.asList(texts.getTexts().split("\\."));
        log.info("splitTextList : {}", splitTextList);

        List<String> audioIdList = new ArrayList<>();

        AudioResponse audioResponse = new AudioResponse();
        for (String s : splitTextList) {
            log.info("splitTextList s : {}", s);
            audioResponse = audioService.getAudio(new AudioRequest(s, "", "result"));
            audioIdList.add(audioResponse.getId()); // 오디오 이름
            log.info("audioResponse.getId() : {}", audioResponse.getId());
        }
        log.info("audioIdList : {}", audioIdList.toArray()); // 오디오 이름


        TextInputResponse textInputResponse = new TextInputResponse();
        textInputResponse.setResult(audioResponse.getStatus());

        ArrayList<TextInputResponse.Audios> resAudioList = new ArrayList<>();
        for (int i = 0; i < audioIdList.size(); i++) {
            resAudioList.add(new TextInputResponse.Audios(Integer.parseInt(audioIdList.get(i)), splitTextList.toString(), audioIdList.toString()));
        }
        textInputResponse.setAudios(resAudioList);

        return textInputResponse;

 */
    }

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

    @Transactional(readOnly = true)
    public GetAvatarPageResponse getAvatarPageData(Long userId, Long projectId) {
        Project findProject = projectRepository.findByUserUidAndId(userId, projectId)
                .orElseThrow(NoSuchElementException::new);

        List<AvatarDto> avatarDtoList = getAvatarDtoList();
        List<BackgroundDto> backgroundDtoList = dummyBackgroundRepository.findAll().stream()
                .map(BackgroundDto::new)
                .collect(Collectors.toList());

        AvatarPageDummyDto dummyData = new AvatarPageDummyDto(avatarDtoList, backgroundDtoList);

        return new GetAvatarPageResponse(findProject, dummyData);
    }

    private List<AvatarDto> getAvatarDtoList() {
        List<AvatarDto> avatarDtoList = new ArrayList<>();

        for (int i = START_AVATAR_IDX; i <= END_AVATAR_IDX; i++) {
            List<DummyAvatarDivision> avatarDivisions = dummyAvatarDivisionRepository.findAllByPositionStartingWith(Integer.toString(i));
            DummyAvatarList dummyAvatar = dummyAvatarListRepository.findByNameEndingWith(Integer.toString(i)).orElseThrow();

            AvatarDto avatar = new AvatarDto(
                    dummyAvatar.getThumbnail(),
                    getAvatarDivisions(avatarDivisions, i + "-1-"),
                    getAvatarDivisions(avatarDivisions, i + "-2-"),
                    getAvatarDivisions(avatarDivisions, i + "-3-")
            );
            avatarDtoList.add(avatar);
        }
        return avatarDtoList;
    }

    private List<AvatarDivisionDto> getAvatarDivisions(List<DummyAvatarDivision> avatarDivisions, String s) {
        return avatarDivisions.stream()
                .filter(a -> a.getPosition().contains(s))
                .map(AvatarDivisionDto::new).collect(Collectors.toList());
    }

}
