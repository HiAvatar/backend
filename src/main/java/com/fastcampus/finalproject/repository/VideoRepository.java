package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
