package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.NativeLoginUser;
import com.fastcampus.finalproject.entity.UserBasic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NativeLoginUserRepository extends JpaRepository<NativeLoginUser, Long> {

    Optional<NativeLoginUser> findById(Long uid);

    Optional<NativeLoginUser> findByUser(UserBasic userBasic);
}
