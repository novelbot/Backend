package com.novelbot.api.dto;

import lombok.Data;

@Data
public class EpisodeDTO {
    private Long episodeId;
    private Long novelId;
    private String episodeTitle;
    private int episodeNumber;
    private String content;
    private String publicationDate;
}
