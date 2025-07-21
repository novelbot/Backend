package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long episode_id;

    private Long novel_id;

    private String episode_title;

    private int episode_number;

    private String content;

    private String publication_date;
}
