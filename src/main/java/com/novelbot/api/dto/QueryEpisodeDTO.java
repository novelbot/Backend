package com.novelbot.api.dto;

import lombok.Data;

@Data
public class QueryEpisodeDTO {
    private Long queryId;
    private Long chatId;
    private Long userId;
    private Long novelId;
    private int episodeNumber;
}
