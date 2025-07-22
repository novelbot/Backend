package com.novelbot.api.dto;

import lombok.Data;

@Data
public class User_reading_processDTO {
    private Long episode_id;
    private Long novel_id;
    private Long user_id;
    private int last_read_page;
    private String updated_at;
}
