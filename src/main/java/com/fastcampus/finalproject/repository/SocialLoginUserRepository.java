package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.SocialLoginUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginUserRepository extends JpaRepository<SocialLoginUser, Long> {
}
