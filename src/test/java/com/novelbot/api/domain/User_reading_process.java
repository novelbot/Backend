package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User_reading_process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long episode_id;

    private Long novel_id;

    private Long user_id;

    private int last_read_page;

    private String updated_at;
}
