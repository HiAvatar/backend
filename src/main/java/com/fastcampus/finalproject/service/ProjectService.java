package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.aws.S3Uploader;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import com.fastcampus.finalproject.dto.AudioDto;
import com.fastcampus.finalproject.dto.VideoDto;
import com.fastcampus.finalproject.dto.request.InsertTextPageRequest;
import com.fastcampus.finalproject.dto.response.CreateProjectResponse;
import com.fastcampus.finalproject.dto.response.GetHistoryResponse;
import com.fastcampus.finalproject.dto.response.GetTextPageResponse;
import com.fastcampus.finalproject.dto.response.InsertTextPageResponse;
import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.entity.Video;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarDivision;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarList;
import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import com.fastcampus.finalproject.enums.SexType;
import com.fastcampus.finalproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fastcampus.finalproject.dto.AudioDto.*;
import static com.fastcampus.finalproject.dto.AvatarPageDto.*;
import static com.fastcampus.finalproject.dto.AvatarPageDto.GetAvatarPageResponse.*;
import static com.fastcampus.finalproject.dto.VideoDto.*;
import static com.fastcampus.finalproject.dto.response.GetTextPageResponse.*;
import static com.fastcampus.finalproject.enums.LanguageType.*;
import static com.fastcampus.finalproject.enums.SexType.FEMALE;
import static com.fastcampus.finalproject.enums.SexType.MALE;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final DummyVoiceRepository dummyVoiceRepository;
    private final DummyAvatarListRepository dummyAvatarListRepository;
    private final DummyAvatarDivisionRepository dummyAvatarDivisionRepository;
    private final DummyBackgroundRepository dummyBackgroundRepository;
    private final FlaskCommunicationService flaskCommunicationService;

    private final S3Uploader s3Uploader;
    private final YmlFlaskConfig localFileConfig;

    private static final int START_AVATAR_IDX = 1;
    private static final int END_AVATAR_IDX = 4;

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

        List<String> textList = Stream.of(findProject.getAudio().getTexts().split("\\."))
                .map(String::trim)
                .collect(Collectors.toList());

        List<Integer> sentenceSpacingList = Stream.of(findProject.getAudio().getSentenceSpacingList().split("\\|"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<TextDto> splitTextList = new ArrayList<>();
        for (int i = 0; i < textList.size(); i++) {
            splitTextList.add(new TextDto(i+1, textList.get(i), sentenceSpacingList.get(i)));
        }

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

        List<AvatarSelectionDto> avatarDtoList = getAvatarDtoList();
        List<BackgroundDto> backgroundDtoList = dummyBackgroundRepository.findAll().stream()
                .map(BackgroundDto::new)
                .collect(Collectors.toList());

        AvatarPageDummyDto dummyData = new AvatarPageDummyDto(avatarDtoList, backgroundDtoList);

        return new GetAvatarPageResponse(findProject, dummyData);
    }

    private List<AvatarSelectionDto> getAvatarDtoList() {
        List<AvatarSelectionDto> avatarDtoList = new ArrayList<>();

        for (int i = START_AVATAR_IDX; i <= END_AVATAR_IDX; i++) {
            List<DummyAvatarDivision> avatarDivisions = dummyAvatarDivisionRepository.findAllByPositionStartingWith(Integer.toString(i));
            DummyAvatarList dummyAvatar = dummyAvatarListRepository.findByNameEndingWith(Integer.toString(i)).orElseThrow();

            AvatarSelectionDto avatar = new AvatarSelectionDto(
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

    @Transactional(readOnly = true)
    public AvatarPreviewResponse getAvatarPreview(AvatarPageRequest request) {
        String filepath = localFileConfig.createImageFilePath(request.getAvatarType(), request.getBgName());
        byte[] fileBinary = getFileBinary(filepath);
        String base64String = Base64.getEncoder().encodeToString(fileBinary);

        return new AvatarPreviewResponse(base64String);
    }

    private static byte[] getFileBinary(String filepath) {
        File file = new File(filepath);
        byte[] bytes = new byte[(int) file.length()];

        try(FileInputStream stream = new FileInputStream(file)) {
            stream.read(bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Transactional
    public InsertTextPageResponse insertTextPageTemp(Long userUid, Long projectId, InsertTextPageRequest request) {
        Project findProject = projectRepository.findByUserUidAndId(userUid, projectId).get();
        findProject.getAudio().changeTexts(request.getTexts());

        AudioResponse audioResponse = flaskCommunicationService.getAudioResult(
                new AudioRequest(findProject.getAudio().getTexts(), "none", "result")
        );

        if(audioResponse.getStatus().equals("Success")) {
            //로컬 파일에서 뒤지기
            File file = new File(localFileConfig.createAudioFilePath(audioResponse.getId()));

            //s3 연동 -> url 받기
            UserBasic user = userRepository.findById(userUid).get();
            Project project = projectRepository.findById(projectId).get();
            String savedFileBucketUrl = s3Uploader.uploadFile(
                    file,
                    user.getUserName() + "_" + userUid + "/" + project.getCreatedAt() + "_" + project.getId(),
                    localFileConfig.getAudioExtension());

            project.changeTotalAudioUrl(savedFileBucketUrl);
            project.changeAudioFileName(audioResponse.getId());
            return new InsertTextPageResponse("Success", savedFileBucketUrl);
        } else {
            return new InsertTextPageResponse("Failed", null);
        }
    }

    @Transactional
    public CompleteAvatarPageResponse insertAvatarPageTemp(Long userUid, Long projectId, AvatarPageRequest request) {
        //ManyToOne으로 User, Project 정보를 끌어온다. -> 1번의 쿼리만으로 조회
        //현재 코드에서는 총 3번의 쿼리
        Project findProject = projectRepository.findByUserUidAndId(userUid, projectId).get();
        findProject.getAvatar().changeAvatarSelection(request.getAvatarName(), request.getAvatarType(), request.getBgName());

        //영상 합성
        VideoResponse videoResponse = flaskCommunicationService.getVideoResult(
                new VideoRequest(findProject.getAudioFileName(), request.getAvatarType(), request.getBgName(), "result")
        );

        if(videoResponse.getStatus().equals("Success")) {
            //로컬 파일에서 뒤지기
            File file = new File(localFileConfig.createVideoFilePath(videoResponse.getId()));

            //s3 연동 -> url 받기
            UserBasic user = userRepository.findById(userUid).get();
            Project project = projectRepository.findById(projectId).get();
            String savedFileBucketUrl = s3Uploader.uploadFile(
                    file,
                    user.getUserName() + "_" + userUid + "/" + project.getCreatedAt() + "_" + project.getId(),
                    localFileConfig.getVideoExtension());

            Video savedVideo = videoRepository.save(new Video(project.getName(), savedFileBucketUrl, user));

            return new CompleteAvatarPageResponse("Success", savedVideo);
        } else {
            return new CompleteAvatarPageResponse("Failed");
        }
    }
}