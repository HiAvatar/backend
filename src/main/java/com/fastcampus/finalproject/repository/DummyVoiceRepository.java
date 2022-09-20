package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.dummy.DummyVoice;
import com.fastcampus.finalproject.enums.LanguageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DummyVoiceRepository extends JpaRepository<DummyVoice, String> {
    List<DummyVoice> findAllByLanguage(String language);
}
