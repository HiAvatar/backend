package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.UserBasic;
import com.fastcampus.finalproject.enums.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserBasic, Long> {

    Optional<UserBasic> findByUserNameAndLoginType(String username, LoginType aNative);

}
