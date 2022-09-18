package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DummyVoiceRepository extends JpaRepository<DummyVoice, String> {
}
