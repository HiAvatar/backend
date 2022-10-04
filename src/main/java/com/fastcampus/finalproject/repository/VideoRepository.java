package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("select v from Video v where v.user.uid = :userUid order by v.createAt desc")
    List<Video> findAllByUserUid(@Param("userUid") Long userUid);
}