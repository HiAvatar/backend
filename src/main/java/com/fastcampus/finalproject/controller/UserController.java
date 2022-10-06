package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.request.NewPasswordDto;
import com.fastcampus.finalproject.dto.request.SignUpInfoDto;
import com.fastcampus.finalproject.dto.request.auth.IdDto;
import com.fastcampus.finalproject.dto.request.auth.TokenPairDto;
import com.fastcampus.finalproject.dto.response.IdAvailabilityDto;
import com.fastcampus.finalproject.dto.response.JwtDto;
import com.fastcampus.finalproject.dto.response.SignUpResultDto;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.enums.LoginType;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/sign-up")
    public ResponseWrapper<SignUpResultDto> signUp(@RequestBody @Valid SignUpInfoDto signUpInfoDto) {
        SignUpResultDto result = userService.signUp(signUpInfoDto);

        return new ResponseWrapper<>(result).ok();
    }

    @PostMapping("/sign-up/check/duplicate-id")
    public ResponseWrapper<IdAvailabilityDto> checkDuplicateId(@RequestBody @Valid IdDto idDto) {
        Optional<UserBasic> userBasic = userRepository.findByUserNameAndLoginType(idDto.getId(), LoginType.NATIVE);
        boolean isIdAvailable = userBasic.isEmpty();

        return new ResponseWrapper<>(new IdAvailabilityDto(isIdAvailable)).ok();
    }

    @PostMapping("/my-page")
    public ResponseWrapper changePassword(@RequestBody @Valid NewPasswordDto newPasswordDto) {
        Long userId = AuthUtil.getCurrentUserUid();
        userService.changePassword(userId, newPasswordDto);

        return new ResponseWrapper().ok();
    }

    @PostMapping("/token/refresh")
    public ResponseWrapper<JwtDto> issueNewTokenPair(@RequestBody TokenPairDto tokenPairDto) {
        JwtDto newTokenPair = userService.issueNewTokenPairs(tokenPairDto);
        return new ResponseWrapper<>(newTokenPair).ok();
    }

    @GetMapping("/test/authorization")
    public String validateAuthorizationProcess() {
        Long uid = AuthUtil.getCurrentUserUid();
        return "Authorization, User ID: " + uid.toString();
    }
}
