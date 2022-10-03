package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.dto.request.*;
import com.fastcampus.finalproject.dto.response.*;
import com.fastcampus.finalproject.dto.response.GetAvatarPageResponse.AvatarDivisionDto;
import com.fastcampus.finalproject.config.YmlLocalFileConfig;
import com.fastcampus.finalproject.dto.request.GetAvatarPreviewRequest;
import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.entity.Video;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarDivision;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarList;
import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import com.fastcampus.finalproject.enums.SexType;
import com.fastcampus.finalproject.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fastcampus.finalproject.dto.response.GetAvatarPageResponse.*;
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
    private final VideoService videoService;
    private final FlaskCommunicationService flaskCommunicationService;

    private final YmlLocalFileConfig localFileConfig;

    private static final int START_AVATAR_IDX = 1;
    private static final int END_AVATAR_IDX = 7;

    @Value("${file.dir}")
    private String fileDir;

    /**
     * 아바타 페이지에서 아바타 정보로 영상 생성
     * */
    @Transactional
    public AvatarInfoResponse addAvatarInfo(Long projectId, AvatarInfoRequest request, Long userUid) throws JsonProcessingException {
        Project findProject = projectRepository.findById(projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));

        String splitTotalAudioUrl = request.splitTotalAudioUrl(findProject.getTotalAudioUrl());

        // 아바타타입에 1-1-1_배경0 이렇게 배경값 추가하기
        VideoResponse videoResponse = videoService.getVideo(new VideoRequest(splitTotalAudioUrl, request.getAvatarType(), request.getBgName(), "result"));

        // 영상 이름으로 특정 경로(로컬)에서 찾아온 후 -> 해당 영상 파일 정보를 DB에 저장
        String videoResponseName = videoResponse.getId() + ".mp4"; // 파일 이름
        log.info("videoResponseName : {}", videoResponseName);
        String videoUrl = fileDir + videoResponseName; // 파일 url

        // 영상 저장된 경로에서 해당 영상 찾아오기
        // 영상 이름, 영상 url(로컬주소), userid, 생성일, 수정일 DB에 저장
        UserBasic user = userRepository.findById(userUid).get();
        Video saveVideo = videoRepository.save(new Video(findProject.getName(), videoUrl, user));

        // DB 에서 정보 빼내서 프론트에 response 내려주기 -> 상태, videoId, videoNAme, video url, 생성일
        AvatarInfoResponse avatarInfoResponse = new AvatarInfoResponse(videoResponse.getStatus(), saveVideo.getId(), saveVideo.getName(), saveVideo.getVideoUrl(), saveVideo.getCreatedAt());
        findProject.getAvatar().changeAvatarInfo(request);

        return avatarInfoResponse;
    }


    /**
     * 프로젝트 이름 변경
     * */
    @Transactional
    public UpdateProjectNameResponse changeProjectName(Long projectId, UpdateProjectNameRequest projectName, Long currentUserUid) {
        Project findProject = projectRepository.findByUserUidAndId(AuthUtil.getCurrentUserUid(), projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));
        findProject.changeProjectName(projectName.getProjectName());
        return new UpdateProjectNameResponse(projectName.getProjectName());
    }

    /**
     * 음성 파일 업로드
     * */
    @Transactional
    public Void addAudioFile(Long projectId, AudioFileUploadRequest audioFile, Long currentUserUid) throws IOException {
        Project findProject = projectRepository.findByUserUidAndId(currentUserUid, projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));

        String audioFileBase64 = audioFile.getAudioFile();
        if (audioFileBase64.isEmpty()) { return null; }

        String audioFileName = audioFile.getAudioFileName();// 순수 파일 이름 추출 (123456)
        String uuid = UUID.randomUUID().toString(); // 저장할 파일 이름에 덧붙일 uuid 생성
        saveUploadAudioFile(audioFileBase64, audioFileName, uuid); // 특정 경로에 파일 저장(현재 local -> 추후에 S3)

        String saveName = uuid + "_" + audioFileName; // 저장할 파일 이름 (uuid_haha.wav)
        findProject.changeAudioFileName(saveName); // DB에 파일 이름 저장
        return null;
    }

    private void saveUploadAudioFile(String audioFileBase64, String audioFileName, String uuid) {
        byte decode[] = java.util.Base64.getDecoder().decode(String.valueOf(audioFileBase64));//.decodeBase64(String.valueOf(audioFileBase64));
        FileOutputStream fos;
        try {
            File target = new File(fileDir + uuid + "_" + audioFileName);
            target.createNewFile();
            fos = new FileOutputStream(target);
            fos.write(decode);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 텍스트 페이지에서 문장별 Audio File 생성
     * */
    @Transactional
    public SentenceInputResponse getAudioFile(SentenceInputRequest sentenceInputRequest) throws Exception {
        // 음성 합성
        AudioResponse audioResponse = audioService.getAudio(new AudioRequest(sentenceInputRequest.getTexts(), "", "result"));

        String audioName = audioResponse.getId(); // 음성 파일 이름
        byte[] audioBinaryFile = getAudioFileBinary(fileDir + audioName + ".wav");
        String audioBase64toString = java.util.Base64.getEncoder().encodeToString(audioBinaryFile);

        SentenceInputResponse sentenceInputResponse = new SentenceInputResponse(audioResponse.getStatus(), audioBase64toString);
        return sentenceInputResponse;
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
     * 텍스트 페이지에서 Audio 정보 추가
     * */
    @Transactional
    public TotalAudioSyntheticResponse addAudioInfo(Long projectId, TotalAudioSyntheticRequest totalAudioInfo, Long currentUserUid) throws Exception {
        Project findProject = projectRepository.findByUserUidAndId(currentUserUid, projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));

        AudioResponse audioResponse = audioService.getAudio(new AudioRequest(totalAudioInfo.getTexts(), "", "result"));
        TotalAudioSyntheticResponse taSyntheticResponse = new TotalAudioSyntheticResponse(audioResponse.getStatus(), "https://../" + audioResponse.getId() + ".wav");

        findProject.getAudio().changeAudioInfo(totalAudioInfo);
        findProject.changeTotalAudioURl("https://../" + audioResponse.getId() + ".wav");

        return taSyntheticResponse;
    }

    /**
     * 처음 텍스트 팝업창으로 저장
     * */
    @Transactional
    public TextInputResponse getAudio(Long projectId, TextInputRequest texts, Long currentUserUid) throws Exception{
        Project findProject = projectRepository.findByUserUidAndId(currentUserUid, projectId).orElseThrow(() -> new NoSuchElementException("프로젝트가 존재하지 않습니다."));

        AudioResponse audioResponse = audioService.getAudio(new AudioRequest(texts.getTexts(), "", "result"));

        TextInputResponse textInputResponse = new TextInputResponse();
        textInputResponse.setResult(audioResponse.getStatus());

        textInputResponse.setTotalAudioUrl("https://../" + audioResponse.getId() + ".wav");
        findProject.changeTotalAudioURl("https://../" + audioResponse.getId() + ".wav");
        return textInputResponse;
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

        //Test Data
        String temp = "하. 하이. 하하이. 하하하이. 하하하하이. 하하하하하이. 하하하하하하이. 하이 하이";
        List<Integer> sentenceSpacingList = Stream.of("0","1","2","3","2","3","-2","-1").map(Integer::parseInt).collect(Collectors.toList());

        //List<String> textList = Stream.of(temp.split("\\.")).map(String::trim).collect(Collectors.toList());
        //List<String> sentenceSpacingList = Stream.of(findProject.getAudio().getSentenceSpacingList().split("\\|")).collect(Collectors.toList());

        List<String> textList = Stream.of(temp.split("\\.")).map(String::trim).collect(Collectors.toList());

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

    public GetAvatarPreviewResponse getAvatarPreview(GetAvatarPreviewRequest request) {
        String filepath = localFileConfig.createFilePath(request.getAvatarType(), request.getBgName());
        byte[] fileBinary = getFileBinary(filepath);
        String base64String = Base64.getEncoder().encodeToString(fileBinary);

        return new GetAvatarPreviewResponse(base64String);
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
}
