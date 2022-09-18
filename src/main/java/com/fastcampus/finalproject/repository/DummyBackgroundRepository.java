package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.dummy.DummyBackground;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DummyBackgroundRepository extends JpaRepository<DummyBackground, String> {
}
