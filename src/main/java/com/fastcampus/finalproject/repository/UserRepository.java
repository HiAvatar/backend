package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.UserBasic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserBasic, Long> {
}
