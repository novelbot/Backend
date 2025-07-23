package com.novelbot.api.dto;

import lombok.Data;

@Data
public class EpisodeDTO {
    private Long episode_id;
    private Long novel_id;
    private String episode_title;
    private int episode_number;
    private String content;
    private String publication_date;
}
