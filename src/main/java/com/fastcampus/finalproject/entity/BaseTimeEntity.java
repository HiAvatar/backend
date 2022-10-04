package com.fastcampus.finalproject.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.ZoneId;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();;
}