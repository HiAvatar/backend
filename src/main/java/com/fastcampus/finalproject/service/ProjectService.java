package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.aws.S3Uploader;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import com.fastcampus.finalproject.dto.request.*;
import com.fastcampus.finalproject.dto.response.*;
import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.entity.Video;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarDivision;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarList;
import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import com.fastcampus.finalproject.enums.ProjectDefaultType;
import com.fastcampus.finalproject.enums.SexType;
import com.fastcampus.finalproject.repository.*;
import com.fastcampus.finalproject.util.CustomTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fastcampus.finalproject.dto.AudioDto.AudioRequest;
import static com.fastcampus.finalproject.dto.AudioDto.AudioResponse;
import static com.fastcampus.finalproject.dto.AvatarPageDto.*;
import static com.fastcampus.finalproject.dto.AvatarPageDto.GetAvatarPageResponse.*;
import static com.fastcampus.finalproject.dto.VideoDto.VideoRequest;
import static com.fastcampus.finalproject.dto.VideoDto.VideoResponse;
import static com.fastcampus.finalproject.dto.response.GetHistoryResponse.ProjectDto;
import static com.fastcampus.finalproject.dto.response.GetHistoryResponse.VideoDto;
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
    private final FlaskCommunicationService flaskCommunicationService;

    private final S3Uploader s3Uploader;
    private final YmlFlaskConfig flaskConfig;

    private static final int START_AVATAR_IDX = 1;
    private static final int END_AVATAR_IDX = 4;

    /**
     * 프로젝트 이름 변경
     */
    @Transactional
    public UpdateProjectNameResponse changeProjectName(Long projectId, UpdateProjectNameRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));
        findProject.changeProjectName(request.getProjectName());
        return new UpdateProjectNameResponse(request.getProjectName());
    }

    /**
     * 음성 파일 업로드
     */
    @Transactional
    public void addAudioFile(Long projectId, AudioFileUploadRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));
        String saveName = saveUploadAudioFile(request.getAudioFile(), request.getAudioFileName(), UUID.randomUUID().toString());

        s3Uploader.removeFile(getProjectPath(findProject), findProject.getAudioFileName() + flaskConfig.getAudioExtension());

        findProject.changeAudioFileName(saveName.substring(saveName.indexOf(".")));
        findProject.changeTotalAudioURl(ProjectDefaultType.EMPTY.getValue());
    }

    private String saveUploadAudioFile(String audioFileBase64, String audioFileName, String uuid) {
        byte[] decode = Base64.getDecoder().decode(String.valueOf(audioFileBase64));
        FileOutputStream fos;
        File target = new File(flaskConfig.getFilePath() + uuid + "_" + audioFileName);

        try {
            target.createNewFile();
            fos = new FileOutputStream(target);
            fos.write(decode);
            fos.close();

            return uuid + "_" + audioFileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 텍스트 페이지에서 한 문장에 대한 Audio File 생성
     */
    @Transactional
    public SentenceInputResponse getAudioFile(SentenceInputRequest request) {
        AudioResponse audioResponse = flaskCommunicationService.getAudioResult(
                new AudioRequest(request.getText(), "none", "result")
        );

        String audioFilePath = flaskConfig.createAudioFilePath(audioResponse.getId());
        byte[] audioBinaryFile = getAudioFileBinary(audioFilePath);
        String audioBase64toString = Base64.getEncoder().encodeToString(audioBinaryFile);

        if (audioResponse.getStatus().equals("Success")) {
            return new SentenceInputResponse(audioResponse.getStatus(), audioBase64toString);
        } else {
            return new SentenceInputResponse(audioResponse.getStatus());
        }
    }

    private static byte[] getAudioFileBinary(String filepath) {
        File file = new File(filepath);
        byte[] data = new byte[(int) file.length()];
        try (FileInputStream stream = new FileInputStream(file)) {
            stream.read(data, 0, data.length);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 처음 텍스트 팝업창으로 저장
     */
    @Transactional
    public TextInputResponse getAudio(Long projectId, TextInputRequest texts) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));

        AudioResponse audioResponse = flaskCommunicationService.getAudioResult(new AudioRequest(texts.getTexts(), "none", "result"));

        if(audioResponse.getStatus().equals("Success")) {
            File file = new File(getFilePath(audioResponse.getId())); //로컬에 있는 파일 찾기
            String savedFileBucketUrl = getSavedFileBucketUrl(file, findProject); //s3 연동 -> url 반환
            s3Uploader.removeFile(getProjectPath(findProject), findProject.getAudioFileName() + flaskConfig.getAudioExtension());

            findProject.changeAudioFileName(audioResponse.getId());
            findProject.changeTotalAudioURl(savedFileBucketUrl);

            return new TextInputResponse(audioResponse.getStatus(), savedFileBucketUrl);
        } else {
            return new TextInputResponse(audioResponse.getStatus());
        }
    }

    @Transactional(readOnly = true)
    public GetHistoryResponse getHistory(Long userUid) {

        List<ProjectDto> projects = projectRepository.findAllByUserUid(userUid)
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());

        List<VideoDto> videos = videoRepository.findAllByUserUid(userUid)
                .stream()
                .map(VideoDto::new)
                .collect(Collectors.toList());

        return new GetHistoryResponse(projects, videos);
    }

    @Transactional
    public CreateProjectResponse create(Long userUid) {
        UserBasic user = userRepository.findById(userUid).get();
        Project savedProject = projectRepository.save(new Project(user));

        savedProject.initProjectName(CustomTimeUtil.convertDateTime(savedProject.getCreatedAt()));
        return new CreateProjectResponse(savedProject.getId(), savedProject.getName());
    }

    @Transactional(readOnly = true)
    public GetTextPageResponse getTextPageData(Long projectId) {
        Project findProject = projectRepository.findById(projectId)
                .orElseThrow(NoSuchElementException::new);

        List<String> textList = Stream.of(findProject.getAudio().getTexts().split("\\."))
                .map(String::trim)
                .collect(Collectors.toList());

        List<Integer> sentenceSpacingList = Stream.of(findProject.getAudio().getSentenceSpacingList().split("\\|"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<TextDto> splitTextList = new ArrayList<>();
        for (int i = 0; i < textList.size(); i++) {
            splitTextList.add(new TextDto(i + 1, textList.get(i), sentenceSpacingList.get(i)));
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
    public GetAvatarPageResponse getAvatarPageData(Long projectId) {
        Project findProject = projectRepository.findById(projectId)
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
        String filepath = flaskConfig.createImageFilePath(request.getAvatarType(), request.getBgName());
        byte[] fileBinary = getAudioFileBinary(filepath);
        String base64String = Base64.getEncoder().encodeToString(fileBinary);

        return new AvatarPreviewResponse(base64String);
    }

    /**
     * 텍스트 페이지에서 Audio 정보 추가
     */
    @Transactional
    public TotalAudioSyntheticResponse addAudioInfo(Long projectId, TotalAudioSyntheticRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));
        request.changeAudioInfo(findProject.getAudio());

        AudioResponse audioResponse = flaskCommunicationService.getAudioResult(
                new AudioRequest(findProject.getAudio().getTexts(), "none", "result")
        );

        if (audioResponse.getStatus().equals("Success")) {
            File file = new File(getFilePath(audioResponse.getId())); //로컬에 있는 파일 찾기
            String savedFileBucketUrl = getSavedFileBucketUrl(file, findProject); //s3 연동 -> url 반환

            //기존 파일 삭제시키기 (s3, local)
            s3Uploader.removeFile(getProjectPath(findProject), findProject.getAudioFileName() + flaskConfig.getAudioExtension());

            findProject.changeAudioFileName(audioResponse.getId());
            findProject.changeTotalAudioURl(savedFileBucketUrl);
            return new TotalAudioSyntheticResponse("Success", savedFileBucketUrl);
        } else {
            return new TotalAudioSyntheticResponse("Failed", null);
        }
    }

    /**
     * 아바타 페이지에서 아바타 정보로 영상 생성
     */
    @Transactional
    public CompleteAvatarPageResponse addAvatarInfo(Long projectId, AvatarPageRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));
        findProject.getAvatar().changeAvatarInfo(request.getAvatarName(), request.getAvatarType(), request.getBgName());

        //영상 합성
        VideoResponse videoResponse = flaskCommunicationService.getVideoResult(
                new VideoRequest(findProject.getAudioFileName(), request.getAvatarType(), request.getBgName(), "result")
        );

        if (videoResponse.getStatus().equals("Success")) {
                File file = new File(flaskConfig.createVideoFilePath(videoResponse.getId()));
            String savedFileBucketUrl = getSavedFileBucketUrl(file, findProject);

            //비디오가 생성되면 더 이상 로컬에 있는 비디오 파일은 무의미. 바로 지워주도록 하자
            s3Uploader.removeLocalFile(file);

            Video savedVideo = videoRepository.save(new Video(findProject.getName(), savedFileBucketUrl, findProject.getUser()));

            return new CompleteAvatarPageResponse("Success", savedVideo);
        } else {
            return new CompleteAvatarPageResponse("Failed");
        }
    }

    private String getFilePath(String id) {
        return flaskConfig.createAudioFilePath(id);
    }

    private String getSavedFileBucketUrl(File file, Project project) {
        return s3Uploader.uploadFile(file, getProjectPath(project));
    }

    private String getProjectPath(Project project) {
        return project.getUser().getUserName() + "_" + project.getUser().getUid() + "/" + project.getCreatedAt() + "_" + project.getId();
    }
}