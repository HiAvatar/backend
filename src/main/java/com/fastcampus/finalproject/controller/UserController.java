package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.dto.ResponseWrapper;
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

import static com.fastcampus.finalproject.dto.AuthDto.JwtDto;
import static com.fastcampus.finalproject.dto.UserDto.*;

@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/sign-up")
    public ResponseWrapper<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        SignUpResponse result = userService.signUp(signUpRequest);

        return new ResponseWrapper<>(result).ok();
    }

    @PostMapping("/sign-up/check/duplicate-id")
    public ResponseWrapper<DuplicateIdResponse> checkDuplicateId(@RequestBody @Valid DuplicateIdRequest duplicateIdRequest) {
        Optional<UserBasic> userBasic = userRepository.findByUserNameAndLoginType(duplicateIdRequest.getId(), LoginType.NATIVE);
        boolean isIdAvailable = userBasic.isEmpty();

        return new ResponseWrapper<>(new DuplicateIdResponse(isIdAvailable)).ok();
    }

    @PostMapping("/my-page")
    public ResponseWrapper<Void> changePassword(@RequestBody @Valid NewPasswordRequest newPasswordRequest) {
        Long userId = AuthUtil.getCurrentUserUid();
        userService.changePassword(userId, newPasswordRequest);

        return new ResponseWrapper<Void>().ok();
    }

    @PostMapping("/token/refresh")
    public ResponseWrapper<JwtDto> issueNewTokenPair(@RequestBody TokenPairResponse tokenPairResponse) {
        JwtDto newTokenPair = userService.issueNewTokenPairs(tokenPairResponse);
        return new ResponseWrapper<>(newTokenPair).ok();
    }

    @PostMapping("/logout")
    public ResponseWrapper<Void> logOut() {
        userService.logout();
        return new ResponseWrapper<Void>().ok();
    }

    @GetMapping("/test/authorization")
    public String validateAuthorizationProcess() {
        Long uid = AuthUtil.getCurrentUserUid();
        return "Authorization, User ID: " + uid.toString();
    }
}
