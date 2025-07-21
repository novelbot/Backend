package com.novelbot.api.dto;

import lombok.Data;

@Data
public class Query_EpisodeDTO {
    private Long query_id;
    private Long chat_id;
    private Long user_id;
    private Long novel_id;
    private int episode_number;
}
