package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.dummy.DummyAvatarDivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DummyAvatarDivisionRepository extends JpaRepository<DummyAvatarDivision, String> {

    List<DummyAvatarDivision> findAllByPositionStartingWith(String start);
}
