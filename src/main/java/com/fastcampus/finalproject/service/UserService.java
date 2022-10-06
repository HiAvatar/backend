package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.dto.request.NewPasswordDto;
import com.fastcampus.finalproject.dto.request.SignUpInfoDto;
import com.fastcampus.finalproject.dto.response.SignUpResultDto;
import com.fastcampus.finalproject.entity.NativeLoginUser;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.enums.LoginType;
import com.fastcampus.finalproject.exception.DuplicateIdException;
import com.fastcampus.finalproject.repository.NativeLoginUserRepository;
import com.fastcampus.finalproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final NativeLoginUserRepository nativeLoginUserRepository;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, NativeLoginUserRepository nativeLoginUserRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.nativeLoginUserRepository = nativeLoginUserRepository;
    }

    public SignUpResultDto signUp(SignUpInfoDto signUpInfoDto) {
        String userId = signUpInfoDto.getId();
        String userPassword = signUpInfoDto.getPassword();

        Optional<UserBasic> userBasic = userRepository.findByUserNameAndLoginType(userId, LoginType.NATIVE);

        if (userBasic.isPresent()) {
            throw new DuplicateIdException("아이디 '" + userId + "'로 가입한 일반 회원이 이미 존재함");
        }

        UserBasic signedUpUser = userRepository.save(new UserBasic(userId, LoginType.NATIVE));
        nativeLoginUserRepository.save(new NativeLoginUser(bCryptPasswordEncoder.encode(userPassword), LocalDateTime.now(), signedUpUser));

        return new SignUpResultDto(signedUpUser.getUserName());
    }

    public boolean changePassword(Long userUid, NewPasswordDto newPasswordDto) {
        Optional<NativeLoginUser> nativeLoginUserOptional = nativeLoginUserRepository.findByUser(new UserBasic(userUid));

        if (nativeLoginUserOptional.isEmpty()) {
            return false;
        }

        NativeLoginUser nativeLoginUser = nativeLoginUserOptional.get();
        nativeLoginUser.changePassword(bCryptPasswordEncoder.encode(newPasswordDto.getNewPassword()));
        nativeLoginUser.changeLastUpdatedTime(LocalDateTime.now());

        nativeLoginUserRepository.save(nativeLoginUser);

        return true;
    }

}
