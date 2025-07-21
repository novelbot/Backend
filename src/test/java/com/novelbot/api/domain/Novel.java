package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Novel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long novel_id;

    private String title;

    private String author;

    private String description;

    private String genre;

    private String cover_image_url;

    // JPA를 위한 기본 생성자
    // public Novel() {}

}