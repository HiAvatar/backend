package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.UserAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisAccessTokenRepository extends CrudRepository<UserAccessToken, Long> {

}
