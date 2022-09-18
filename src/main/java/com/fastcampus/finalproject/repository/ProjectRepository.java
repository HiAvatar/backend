package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
