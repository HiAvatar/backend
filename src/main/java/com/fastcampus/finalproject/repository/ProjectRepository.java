package com.fastcampus.finalproject.repository;

import com.fastcampus.finalproject.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select p from Project p where p.user.uid = :userUid order by p.lastModifiedAt desc")
    List<Project> findAllByUserUid(@Param("userUid") Long userUid);

    Optional<Project> findByUserUidAndId(Long userUid, Long projectId);
}
