package com.fastcampus.finalproject.controller;

import com.fastcampus.finalproject.config.security.AuthUtil;
import com.fastcampus.finalproject.config.security.jwt.utils.AccessTokenUtility;
import com.fastcampus.finalproject.config.security.jwt.utils.RefreshTokenUtility;
import com.fastcampus.finalproject.dto.ResponseWrapper;
import com.fastcampus.finalproject.dto.request.NewPasswordDto;
import com.fastcampus.finalproject.dto.request.SignUpInfoDto;
import com.fastcampus.finalproject.dto.request.auth.IdDto;
import com.fastcampus.finalproject.dto.response.IdAvailabilityDto;
import com.fastcampus.finalproject.dto.response.JwtDto;
import com.fastcampus.finalproject.dto.response.SignUpResultDto;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.entity.UserRefreshToken;
import com.fastcampus.finalproject.enums.LoginType;
import com.fastcampus.finalproject.repository.NativeLoginUserRepository;
import com.fastcampus.finalproject.repository.RefreshTokenRepository;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {

    private final AccessTokenUtility accessTokenUtility;
    private final RefreshTokenUtility refreshTokenUtility;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final NativeLoginUserRepository nativeLoginUserRepository;

    public UserController(AccessTokenUtility accessTokenUtility, RefreshTokenUtility refreshTokenUtility, UserService userService, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, NativeLoginUserRepository nativeLoginUserRepository) {
        this.accessTokenUtility = accessTokenUtility;
        this.refreshTokenUtility = refreshTokenUtility;
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.nativeLoginUserRepository = nativeLoginUserRepository;
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
    public ResponseWrapper<JwtDto> issueNewTokenPair(@RequestBody String refreshToken) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SecurityException, IllegalArgumentException {
        refreshTokenUtility.validateToken(refreshToken); // throws exceptions, exceptions will be handled in controller advice

        Long userUid = refreshTokenUtility.extractSubValueFromPayload(refreshToken);
        Authentication newAuthToken = new UsernamePasswordAuthenticationToken(userUid, new SimpleGrantedAuthority("ROLE_USER"));

        String newAccessToken = accessTokenUtility.createJsonWebToken(newAuthToken);
        String newRefreshToken = refreshTokenUtility.createRefreshJsonWebToken(newAuthToken);

        refreshTokenRepository.save(new UserRefreshToken(userUid, newRefreshToken));

        return new ResponseWrapper<>(new JwtDto(newAccessToken, newRefreshToken));
    }

    @GetMapping("/test/authorization")
    public String validateAuthorizationProcess() {
        Long uid = AuthUtil.getCurrentUserUid();
        return "Authorization, User ID: " + uid.toString();
    }
}
