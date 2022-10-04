package com.fastcampus.finalproject.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
public class BaseTimeEntity {

//    @CreatedDate
//    @Column(updatable = false, nullable = false)
//    private LocalDateTime createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
//
//    @LastModifiedDate
//    @Column(nullable = false)
//    private LocalDateTime lastModifiedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    private ZonedDateTime createAt;
    private ZonedDateTime lastModifiedAt;

    @PrePersist
    public void prePersist() {
        this.createAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        this.lastModifiedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @PreUpdate
    public void preUpdate() {
        this.lastModifiedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}