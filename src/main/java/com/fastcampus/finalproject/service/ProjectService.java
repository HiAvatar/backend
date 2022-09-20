package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.dto.response.GetHistoryResponse;
import com.fastcampus.finalproject.repository.ProjectRepository;
import com.fastcampus.finalproject.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final VideoRepository videoRepository;

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
}
