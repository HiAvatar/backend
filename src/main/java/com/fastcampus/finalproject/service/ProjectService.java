package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.aws.S3Uploader;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.dto.request.*;
import com.fastcampus.finalproject.dto.response.*;
import com.fastcampus.finalproject.entity.Project;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.entity.Video;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarDivision;
import com.fastcampus.finalproject.entity.dummy.DummyAvatarList;
import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import com.fastcampus.finalproject.enums.FileType;
import com.fastcampus.finalproject.enums.ProjectDefaultType;
import com.fastcampus.finalproject.enums.SexType;
import com.fastcampus.finalproject.exception.NoCorrectProjectAccessException;
import com.fastcampus.finalproject.exception.NoCreateUploadAudioFileException;
import com.fastcampus.finalproject.exception.NoGetAudioFileBinaryException;
import com.fastcampus.finalproject.exception.NotSameSizeTwoListsException;
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
     * 신규 프로젝트 생성
     */
    @Transactional
    public CreateProjectResponse create(Long userUid) {
        UserBasic user = userRepository.findById(userUid).get();
        Project savedProject = projectRepository.save(new Project(user));

        List<Project> projects = projectRepository.findAllByUserUid(userUid);
        deleteOldestProject(projects);

        savedProject.initProjectName(CustomTimeUtil.convertDateTime(savedProject.getCreatedAt()));
        return new CreateProjectResponse(savedProject.getId(), savedProject.getName());
    }

    private void deleteOldestProject(List<Project> projects) {
        if (projects.size() > 5) {
            Project removeProject = projects.get(projects.size() - 1);
            log.info("삭제된 프로젝트 id = {}", removeProject.getId());
            projectRepository.delete(removeProject);
            projects.remove(projects.size() - 1);

            //프로젝트가 삭제되면 s3 폴더 삭제
            s3Uploader.removeFile(beMappedS3AudioPath(removeProject), FileType.AUDIO, removeProject.getAudioFileName(), flaskConfig.getAudioExtension());
        }
    }

    /**
     * 프로젝트 이름 변경
     */
    @Transactional
    public UpdateProjectNameResponse changeProjectName(Long projectId, UpdateProjectNameRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        findProject.changeProjectName(request.getProjectName());
        return new UpdateProjectNameResponse(request.getProjectName());
    }

    /**
     * 프로젝트 및 영상 히스토리 조회
     */
    @Transactional
    public GetHistoryResponse getHistory(Long userUid) {
        List<ProjectDto> projectDtos = projectRepository.findAllByUserUid(userUid)
                .stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());

        List<VideoDto> videoDtos = videoRepository.findAllByUserUid(userUid)
                .stream()
                .map(VideoDto::new)
                .collect(Collectors.toList());

        return new GetHistoryResponse(projectDtos, videoDtos);
    }

    /**
     * 텍스트 페이지에서 Audio 정보 추가 및 음성 생성
     */
    @Transactional
    public TotalAudioSyntheticResponse addAudioInfo(Long projectId, TotalAudioSyntheticRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());
        validateSameTextsAndSplitTextList(request);

        request.changeAudioInfo(findProject.getAudio());

        AudioResponse audioResponse = flaskCommunicationService.getAudioResult(
                new AudioRequest(findProject.getAudio().getTexts(), "none", "result")
        );

        if (audioResponse.getStatus().equals("Success")) {
            String savedFileBucketUrl = saveAudioFileToS3(findProject, audioResponse.getId());
            return new TotalAudioSyntheticResponse("Success", savedFileBucketUrl);
        } else {
            return new TotalAudioSyntheticResponse("Failed", null);
        }
    }

    private void validateSameTextsAndSplitTextList(TotalAudioSyntheticRequest request) {
        List<String> textList = Stream.of(request.getTexts().split("\\."))
                .map(String::trim)
                .collect(Collectors.toList());

        if(textList.size() != request.getSplitTextList().size()) {
            throw new NotSameSizeTwoListsException();
        }
    }

    private String saveAudioFileToS3(Project findProject, String audioId) {
        File file = new File(flaskConfig.createAudioFilePath(audioId)); //로컬에 있는 파일 찾기
        String savedFileBucketUrl = getSavedFileBucketUrl(file, FileType.AUDIO, findProject); //s3 연동 -> url 반환

        //기존 파일 삭제시키기 (s3, local)
        s3Uploader.removeFile(beMappedS3AudioPath(findProject), FileType.AUDIO, findProject.getAudioFileName(), flaskConfig.getAudioExtension());

        findProject.changeAudioFileName(audioId);
        findProject.changeTotalAudioURl(savedFileBucketUrl);
        return savedFileBucketUrl;
    }

    /**
     * 음성 파일 업로드
     */
    @Transactional
    public void uploadAudioFile(Long projectId, AudioFileUploadRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        String saveName = saveUploadAudioFile(request.getAudioFile(), request.getAudioFileName(), UUID.randomUUID().toString());
        s3Uploader.removeFile(beMappedS3AudioPath(findProject), FileType.AUDIO, findProject.getAudioFileName(), flaskConfig.getAudioExtension());

        findProject.changeAudioFileName(saveName.substring(0, saveName.lastIndexOf(".")));
        findProject.changeTotalAudioURl(ProjectDefaultType.EMPTY.getValue());
    }

    private String saveUploadAudioFile(String audioFileBase64, String audioFileName, String uuid) {
        byte[] decode = Base64.getDecoder().decode(String.valueOf(audioFileBase64));
        File target = new File(flaskConfig.getFilePath() + uuid + "_" + audioFileName);
        try (FileOutputStream fos = new FileOutputStream(target)){
            fos.write(decode);
            fos.close();
            return uuid + "_" + audioFileName;
        } catch (IOException e) {
            throw new NoCreateUploadAudioFileException(e);
        }
    }

    /**
     * 텍스트 페이지에서 한 문장에 대한 Audio File 생성
     */
    @Transactional
    public SentenceInputResponse getAudioFileAboutOneSentence(SentenceInputRequest request) {
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
        } catch (IOException e) {
            throw new NoGetAudioFileBinaryException(e);
        }
        return data;
    }

    /**
     * 처음 텍스트 팝업창으로 저장
     */
    @Transactional
    public TextInputResponse getAudioFileAboutScript(Long projectId, TextInputRequest texts) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        findProject.getAudio().changeTexts(texts.getTexts());

        AudioResponse audioResponse = flaskCommunicationService.getAudioResult(new AudioRequest(texts.getTexts(), "none", "result"));

        if (audioResponse.getStatus().equals("Success")) {
            String savedFileBucketUrl = saveAudioFileToS3(findProject, audioResponse.getId());
            return new TextInputResponse(audioResponse.getStatus(), savedFileBucketUrl);
        } else {
            return new TextInputResponse(audioResponse.getStatus());
        }
    }

    /**
     * 아바타 페이지에서 아바타 정보로 영상 생성
     */
    @Transactional
    public CompleteAvatarPageResponse addAvatarInfo(Long projectId, AvatarPageRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        findProject.getAvatar().changeAvatarInfo(request.getAvatarName(), request.getAvatarType(), request.getBgName());

        //영상 합성
        VideoResponse videoResponse = flaskCommunicationService.getVideoResult(
                new VideoRequest(findProject.getAudioFileName(), request.getAvatarType(), request.getBgName(), "result")
        );

        if (videoResponse.getStatus().equals("Success")) {
            File file = new File(flaskConfig.createVideoFilePath(videoResponse.getId()));
            String savedFileBucketUrl = getSavedFileBucketUrl(file, FileType.VIDEO, findProject);

            s3Uploader.removeLocalFile(file); // 비디오가 생성되면 더 이상 로컬에 있는 비디오 파일은 무의미. 바로 지워주도록 하자
            Video savedVideo = videoRepository.save(new Video(findProject.getName(), file.getName().substring(0, file.getName().lastIndexOf(".")), savedFileBucketUrl, findProject.getUser()));
            return new CompleteAvatarPageResponse(videoResponse.getStatus(), savedVideo);
        } else {
            return new CompleteAvatarPageResponse(videoResponse.getStatus());
        }
    }

    @Transactional
    public void deleteOldestVideo(Long userUid) {
        List<Video> videos = videoRepository.findAllByUserUid(userUid);
        if (videos.size() > 5) {
            Video removeVideo = videos.get(videos.size() - 1);
            videoRepository.delete(removeVideo);
            log.info("삭제된 영상 id = {}", removeVideo.getId());

            //비디오가 삭제되면 s3의 프로젝트 폴더에서 해당 video 삭제
            s3Uploader.removeFile(beMappedS3VideoPath(removeVideo.getUser()), FileType.VIDEO, removeVideo.getVideoFileName(), flaskConfig.getVideoExtension());
        }
    }

    /**
     * 텍스트 페이지 임시 저장
     * */
    @Transactional
    public void saveAudioInfo(Long projectId, TotalAudioSyntheticRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        request.changeAudioInfo(findProject.getAudio());
    }

    /**
     * 아바타 페이지 임시 저장
     * */
    @Transactional
    public void addTempAvatarInfo(Long projectId, AvatarPageRequest request) {
        Project findProject = projectRepository.findById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        findProject.getAvatar().changeAvatarName(request.getAvatarName());
        getTempAvatarInfoOrBlank(request, findProject);
    }

    private void getTempAvatarInfoOrBlank(AvatarPageRequest request, Project findProject) {
        if (!(request.getAvatarType().isBlank())) {
            findProject.getAvatar().changeTempAvatarTypeNotNull(request.getAvatarType());
        }
        if (!(request.getBgName().isBlank())) {
            findProject.getAvatar().changeTempBgNameNotNull(request.getBgName());
        }
    }

    /**
     * 텍스트 페이지에 관한 데이터 조회
     */
    @Transactional(readOnly = true)
    public GetTextPageResponse getTextPageData(Long projectId) {
        Project findProject = projectRepository.findById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        List<TextDto> splitTextList = getSplitTextDtos(findProject);
        TextPageDummyDto dummyData = getTextPageDummyDto();

        return new GetTextPageResponse(findProject, splitTextList, dummyData);
    }

    private List<TextDto> getSplitTextDtos(Project findProject) {
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
        return splitTextList;
    }

    private TextPageDummyDto getTextPageDummyDto() {
        List<DummyVoice> koreanList = dummyVoiceRepository.findAllByLanguage(KOREAN.getValue());
        List<DummyVoice> englishList = dummyVoiceRepository.findAllByLanguage(ENGLISH.getValue());
        List<DummyVoice> chineseList = dummyVoiceRepository.findAllByLanguage(CHINESE.getValue());

        KoreanDto korean = new KoreanDto(getGenderList(koreanList, FEMALE), getGenderList(koreanList, MALE));
        EnglishDto english = new EnglishDto(getGenderList(englishList, FEMALE), getGenderList(englishList, MALE));
        ChineseDto chinese = new ChineseDto(getGenderList(chineseList, FEMALE), getGenderList(chineseList, MALE));

        return new TextPageDummyDto(korean, english, chinese);
    }

    private List<CharacterDto> getGenderList(List<DummyVoice> list, SexType type) {
        return list.stream().filter(v -> v.getSex().equals(type.getValue()))
                .map(CharacterDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 아바타 선택 페이지에 관한 데이터 조회
     */
    @Transactional(readOnly = true)
    public GetAvatarPageResponse getAvatarPageData(Long projectId) {
        Project findProject = projectRepository.findById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

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

    /**
     * 아바타 미리보기
     */
    @Transactional(readOnly = true)
    public AvatarPreviewResponse getAvatarPreview(AvatarPageRequest request) {
        String filepath = flaskConfig.createImageFilePath(request.getAvatarType(), request.getBgName());
        byte[] fileBinary = getAudioFileBinary(filepath);
        String base64String = Base64.getEncoder().encodeToString(fileBinary);

        return new AvatarPreviewResponse(base64String);
    }

    private void validateAccessOnCurrentUser(UserBasic user) {
        if(!user.getUid().equals(AuthUtil.getCurrentUserUid())) {
            throw new NoCorrectProjectAccessException();
        }
    }

    private String getSavedFileBucketUrl(File file, FileType fileType, Project project) {
        if (fileType == FileType.AUDIO)
            return s3Uploader.uploadFile(file, fileType, beMappedS3AudioPath(project));
        else
            return s3Uploader.uploadFile(file, fileType, beMappedS3VideoPath(project.getUser()));
    }

    private String beMappedS3AudioPath(Project project) {
        return project.getUser().getUserName() +
                "_" +
                project.getUser().getUid() +
                "/" +
                CustomTimeUtil.convertDateTime(project.getCreatedAt()) +
                "_" +
                project.getId();
    }

    private String beMappedS3VideoPath(UserBasic user) {
        return user.getUserName() + "_" + user.getUid();
    }
}