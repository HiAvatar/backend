package com.fastcampus.finalproject.service.auth;

import com.fastcampus.finalproject.entity.NativeLoginUser;
import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.enums.LoginType;
import com.fastcampus.finalproject.repository.NativeLoginUserRepository;
import com.fastcampus.finalproject.repository.UserRepository;
import com.fastcampus.finalproject.service.auth.dto.NativeUserContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final NativeLoginUserRepository nativeLoginUserRepository;

    public CustomUserDetailsService(UserRepository userRepository, NativeLoginUserRepository nativeLoginUserRepository) {
        this.userRepository = userRepository;
        this.nativeLoginUserRepository = nativeLoginUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        Optional<UserBasic> userBasicOptional = userRepository.findByUserNameAndLoginType(username, LoginType.NATIVE);
        UserBasic userBasic = userBasicOptional.orElseThrow(() -> new UsernameNotFoundException("데이터베이스에서 사용자 정보를 찾을 수 없음"));

        Optional<NativeLoginUser> nativeLoginUserOptional = nativeLoginUserRepository.findByUser(userBasic);
        NativeLoginUser nativeLoginUser = nativeLoginUserOptional.orElseThrow(() -> new RuntimeException("일반 로그인 인증정보 테이블에서 UID " + userBasic.getUid() + " 사용자의 정보를 찾을 수 없음"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new NativeUserContext(userBasic.getUid(), userBasic.getUserName(), nativeLoginUser.getPassword(), authorities);
    }
}
