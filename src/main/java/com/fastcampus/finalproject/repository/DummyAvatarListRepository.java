package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.dummy.DummyAvatarList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DummyAvatarListRepository extends JpaRepository<DummyAvatarList, String> {

    Optional<DummyAvatarList> findByNameEndingWith(String end);
}
