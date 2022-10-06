package com.fastcampus.finalproject.service;

import com.fastcampus.finalproject.config.security.jwt.utils.AccessTokenUtility;
import com.fastcampus.finalproject.config.security.jwt.utils.RefreshTokenUtility;
import com.fastcampus.finalproject.config.security.jwt.utils.TokenValidityCode;
import com.fastcampus.finalproject.dto.request.NewPasswordDto;
import com.fastcampus.finalproject.dto.request.SignUpInfoDto;
import com.fastcampus.finalproject.dto.request.auth.TokenPairDto;
import com.fastcampus.finalproject.dto.response.JwtDto;
import com.fastcampus.finalproject.dto.response.SignUpResultDto;
import com.fastcampus.finalproject.entity.NativeLoginUser;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.entity.UserRefreshToken;
import com.fastcampus.finalproject.enums.LoginType;
import com.fastcampus.finalproject.exception.token.AccessTokenInvalidException;
import com.fastcampus.finalproject.exception.token.AccessTokenStillValidException;
import com.fastcampus.finalproject.exception.DuplicateIdException;
import com.fastcampus.finalproject.exception.token.RefreshTokenInvalidException;
import com.fastcampus.finalproject.repository.NativeLoginUserRepository;
import com.fastcampus.finalproject.repository.RefreshTokenRepository;
import com.fastcampus.finalproject.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final NativeLoginUserRepository nativeLoginUserRepository;
    private final AccessTokenUtility accessTokenUtility;
    private final RefreshTokenUtility refreshTokenUtility;
    private final RefreshTokenRepository refreshTokenRepository;


    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, NativeLoginUserRepository nativeLoginUserRepository, AccessTokenUtility accessTokenUtility, RefreshTokenUtility refreshTokenUtility, RefreshTokenRepository refreshTokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.nativeLoginUserRepository = nativeLoginUserRepository;
        this.accessTokenUtility = accessTokenUtility;
        this.refreshTokenUtility = refreshTokenUtility;
        this.refreshTokenRepository = refreshTokenRepository;
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

    public JwtDto issueNewTokenPairs(TokenPairDto tokenPairDto) {
        String accessToken = tokenPairDto.getAccessToken();
        String refreshToken = tokenPairDto.getRefreshToken();

        switch (accessTokenUtility.evaluateTokenValidity(accessToken)) {
            case VALID:
                throw new AccessTokenStillValidException("액세스 토큰이 아직 유효합니다.");
            case INVALID:
                throw new AccessTokenInvalidException("유효하지 않은 액세스 토큰입니다");
            default: // It falls through this switch statement when the access token is expired but valid.
                break;
        }

        if (refreshTokenUtility.evaluateTokenValidity(refreshToken) != TokenValidityCode.VALID) {
            throw new RefreshTokenInvalidException("유효하지 않은 리프레시 토큰입니다");
        }

        Long userUid = refreshTokenUtility.extractSubValueFromPayload(refreshToken);
        Authentication newAuthToken = new UsernamePasswordAuthenticationToken(userUid, new SimpleGrantedAuthority("ROLE_USER"));

        String newAccessToken = accessTokenUtility.createJsonWebToken(newAuthToken);
        String newRefreshToken = refreshTokenUtility.createRefreshJsonWebToken(newAuthToken);

        refreshTokenRepository.save(new UserRefreshToken(userUid, newRefreshToken));

        return new JwtDto(newAccessToken, newRefreshToken);
    }

}
