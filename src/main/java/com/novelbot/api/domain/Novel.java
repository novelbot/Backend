package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String description;
    private String genre;
    private String coverImageUrl;

    @Builder
    public Novel(String title, String author, String description, String genre, String coverImageUrl) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.coverImageUrl = coverImageUrl;
    }
}

