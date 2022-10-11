package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.UserRefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRefreshTokenRepository extends CrudRepository<UserRefreshToken, Long> {

}
