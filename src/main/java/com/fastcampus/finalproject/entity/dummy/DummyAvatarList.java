package com.fastcampus.finalproject.entity.dummy;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class DummyAvatarList {

    @Id
    private String name;

    private String thumbnail;
}
