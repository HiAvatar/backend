package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.aws.S3Uploader;
import com.fastcampus.finalproject.config.YmlFlaskConfig;
import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.dto.ProjectHistoryDto.*;
import com.fastcampus.finalproject.dto.TextPageDto.*;
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
import static com.fastcampus.finalproject.dto.ProjectHistoryDto.GetHistoryResponse.ProjectDto;
import static com.fastcampus.finalproject.dto.ProjectHistoryDto.GetHistoryResponse.VideoDto;
import static com.fastcampus.finalproject.dto.TextPageDto.GetTextPageResponse.*;
import static com.fastcampus.finalproject.enums.FlaskResponseType.*;
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
    private final FlaskService flaskService;

    private final S3Uploader s3Uploader;
    private final YmlFlaskConfig flaskConfig;

    private static final int START_AVATAR_IDX = 1;
    private static final int END_AVATAR_IDX = 4;

    /**
     * ?????? ???????????? ??????
     */
    @Transactional
    public CreateProjectResponse create(Long userUid) {
        UserBasic user = userRepository.findById(userUid).orElseThrow();
        Project savedProject = projectRepository.save(new Project(user));

        List<Project> projects = projectRepository.findAllByUserUid(userUid);
        deleteOldestProject(projects);

        savedProject.initProjectName(CustomTimeUtil.convertDateTime(savedProject.getCreatedAt()));
        return new CreateProjectResponse(savedProject.getId(), savedProject.getName());
    }

    private void deleteOldestProject(List<Project> projects) {
        if (projects.size() > 5) {
            Project removeProject = projects.get(projects.size() - 1);
            projectRepository.delete(removeProject);
            log.info("????????? ???????????? id = {}", removeProject.getId());

            //??????????????? ???????????? s3 ?????? ??????
            s3Uploader.removeFile(beMappedS3AudioPath(removeProject), FileType.AUDIO, removeProject.getAudioFileName(), flaskConfig.getAudioExtension());
        }
    }

    /**
     * ???????????? ?????? ??????
     */
    @Transactional
    public UpdateProjectNameResponse changeProjectName(Long projectId, UpdateProjectNameRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        findProject.changeProjectName(request.getProjectName());
        return new UpdateProjectNameResponse(request.getProjectName());
    }

    /**
     * ???????????? ??? ?????? ???????????? ??????
     */
    @Transactional(readOnly = true)
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
     * ????????? ??????????????? Audio ?????? ?????? ??? ?????? ??????
     */
    @Transactional
    public TotalAudioSyntheticResponse addAudioInfo(Long projectId, TotalAudioSyntheticRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());
        validateSameTextsAndSplitTextList(request);

        request.changeAudioInfo(findProject.getAudio());

        AudioRequest audioRequest = flaskService.getAudioRequest(findProject.getAudio().getTexts());
        return getTotalAudioSyntheticResponse(findProject, flaskService.getAudioResponse(audioRequest));
    }

    private TotalAudioSyntheticResponse getTotalAudioSyntheticResponse(Project findProject, AudioResponse audioResponse) {
        if (audioResponse.getStatus().equals(SUCCESS.getValue())) {
            String savedFileBucketUrl = saveAudioFileToS3(findProject, audioResponse.getId());
            return new TotalAudioSyntheticResponse(SUCCESS.getValue(), savedFileBucketUrl);
        } else {
            return new TotalAudioSyntheticResponse(FAILED.getValue());
        }
    }

    private void validateSameTextsAndSplitTextList(TotalAudioSyntheticRequest request) {
        List<String> textList = Stream.of(request.getTexts().split("\\."))
                .map(String::trim)
                .collect(Collectors.toList());

        if (textList.size() != request.getSplitTextList().size()) {
            throw new NotSameSizeTwoListsException();
        }
    }

    private String saveAudioFileToS3(Project findProject, String audioId) {
        File file = new File(flaskConfig.createAudioFilePath(audioId)); //????????? ?????? ?????? ??????
        String savedFileBucketUrl = getSavedFileBucketUrl(file, FileType.AUDIO, findProject); //s3 ?????? -> url ??????

        //?????? ?????? ??????????????? (s3, local)
        s3Uploader.removeFile(beMappedS3AudioPath(findProject), FileType.AUDIO, findProject.getAudioFileName(), flaskConfig.getAudioExtension());

        findProject.changeAudioFileName(audioId);
        findProject.changeTotalAudioURl(savedFileBucketUrl);
        return savedFileBucketUrl;
    }

    /**
     * ?????? ?????? ?????????
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
        try (FileOutputStream fos = new FileOutputStream(target)) {
            fos.write(decode);
            fos.close();
            return uuid + "_" + audioFileName;
        } catch (IOException e) {
            throw new NoCreateUploadAudioFileException(e);
        }
    }

    /**
     * ????????? ??????????????? ??? ????????? ?????? Audio File ??????
     */
    @Transactional
    public SentenceInputResponse getAudioFileAboutOneSentence(SentenceInputRequest request) {
        AudioRequest audioRequest = flaskService.getAudioRequest(request.getText());
        AudioResponse audioResponse = flaskService.getAudioSynthetic(audioRequest);

        String audioFilePath = flaskConfig.createAudioFilePath(audioResponse.getId());
        File file = new File(audioFilePath);
        byte[] audioBinaryFile = getAudioFileBinary(file);
        String audioBase64toString = Base64.getEncoder().encodeToString(audioBinaryFile);
        s3Uploader.removeLocalFile(file);

        return getSentenceInputResponse(audioResponse, audioBase64toString);
    }

    private static byte[] getAudioFileBinary(File file) {
        byte[] data = new byte[(int) file.length()];
        try (FileInputStream stream = new FileInputStream(file)) {
            stream.read(data, 0, data.length);
        } catch (IOException e) {
            throw new NoGetAudioFileBinaryException(e);
        }
        return data;
    }

    private SentenceInputResponse getSentenceInputResponse(AudioResponse audioResponse, String audioBase64toString) {
        if (audioResponse.getStatus().equals("Success")) {
            return new SentenceInputResponse(audioResponse.getStatus(), audioBase64toString);
        } else {
            return new SentenceInputResponse(audioResponse.getStatus());
        }
    }

    /**
     * ?????? ????????? ??????????????? ??????
     */
    @Transactional
    public TextInputResponse getAudioFileAboutScript(Long projectId, TextInputRequest texts) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        findProject.getAudio().changeTexts(texts.getTexts());

        AudioRequest audioRequest = flaskService.getAudioRequest(findProject.getAudio().getTexts());
        AudioResponse audioResponse = flaskService.getAudioSynthetic(audioRequest);

        return getTextInputResponse(findProject, audioResponse);
    }

    private TextInputResponse getTextInputResponse(Project findProject, AudioResponse audioResponse) {
        if (audioResponse.getStatus().equals("Success")) {
            String savedFileBucketUrl = saveAudioFileToS3(findProject, audioResponse.getId());
            return new TextInputResponse(audioResponse.getStatus(), savedFileBucketUrl);
        } else {
            return new TextInputResponse(audioResponse.getStatus());
        }
    }

    /**
     * ????????? ??????????????? ????????? ????????? ?????? ??????
     */
    @Transactional
    public CompleteAvatarPageResponse addAvatarInfo(Long projectId, AvatarPageRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        findProject.getAvatar().changeAvatarInfo(request.getAvatarName(), request.getAvatarType(), request.getBgName());

        VideoRequest videoRequest = flaskService.getVideoRequest(findProject.getAudioFileName(), request.getAvatarType(), request.getBgName());
        VideoResponse videoResponse = flaskService.getVideoResponse(videoRequest);

        return getCompleteAvatarPageResponse(findProject, videoResponse);
    }

    private CompleteAvatarPageResponse getCompleteAvatarPageResponse(Project findProject, VideoResponse videoResponse) {
        if (videoResponse.getStatus().equals("Success")) {
            File file = new File(flaskConfig.createVideoFilePath(videoResponse.getId()));
            Video savedVideo = videoRepository.save(getVideo(findProject, file));
            s3Uploader.removeLocalFile(file); // ???????????? ???????????? ??? ?????? ????????? ?????? ????????? ????????? ?????????. ?????? ??????????????? ??????

            return new CompleteAvatarPageResponse(videoResponse.getStatus(), savedVideo);
        } else {
            return new CompleteAvatarPageResponse(videoResponse.getStatus());
        }
    }

    private Video getVideo(Project findProject, File file) {
        return Video.builder()
                .name(findProject.getName())
                .videoFileName(file.getName().substring(0, file.getName().lastIndexOf(".")))
                .videoUrl(getSavedFileBucketUrl(file, FileType.VIDEO, findProject))
                .user(findProject.getUser())
                .build();
    }

    @Transactional
    public void deleteOldestVideo(Long userUid) {
        List<Video> videos = videoRepository.findAllByUserUid(userUid);
        if (videos.size() > 5) {
            Video removeVideo = videos.get(videos.size() - 1);
            videoRepository.delete(removeVideo);
            log.info("????????? ?????? id = {}", removeVideo.getId());

            //???????????? ???????????? s3??? ???????????? ???????????? ?????? video ??????
            s3Uploader.removeFile(beMappedS3VideoPath(removeVideo.getUser()), FileType.VIDEO, removeVideo.getVideoFileName(), flaskConfig.getVideoExtension());
        }
    }

    /**
     * ????????? ????????? ?????? ??????
     */
    @Transactional
    public void saveAudioInfo(Long projectId, TotalAudioSyntheticRequest request) {
        Project findProject = projectRepository.findWithUserById(projectId).orElseThrow(NoSuchElementException::new);
        validateAccessOnCurrentUser(findProject.getUser());

        request.changeAudioInfo(findProject.getAudio());
    }

    /**
     * ????????? ????????? ?????? ??????
     */
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
     * ????????? ???????????? ?????? ????????? ??????
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
     * ????????? ?????? ???????????? ?????? ????????? ??????
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
     * ????????? ????????????
     */
    @Transactional(readOnly = true)
    public AvatarPreviewResponse getAvatarPreview(AvatarPageRequest request) {
        String filepath = flaskConfig.createImageFilePath(request.getAvatarType(), request.getBgName());
        byte[] fileBinary = getAudioFileBinary(new File(filepath));
        String base64String = Base64.getEncoder().encodeToString(fileBinary);

        return new AvatarPreviewResponse(base64String);
    }

    private void validateAccessOnCurrentUser(UserBasic user) {
        if (!user.getUid().equals(AuthUtil.getCurrentUserUid())) {
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