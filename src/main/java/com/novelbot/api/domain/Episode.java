package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "episode_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private Novel novel;

    private String title;

    private int episodeNumber;

    private String content;

    private LocalDate publicationDate;

    @Builder
    public Episode(Novel novel, String title, int episodeNumber, String content, LocalDate publicationDate) {
        this.novel = novel;
        this.title = title;
        this.episodeNumber = episodeNumber;
        this.content = content;
        this.publicationDate = publicationDate;
    }
}

