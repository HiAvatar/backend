package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.dto.response.UserInfoResponse;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userUid) {
        UserBasic user = userRepository.findById(userUid).orElseThrow(NoSuchElementException::new);
        return new UserInfoResponse(user.getUserName());
    }
}
