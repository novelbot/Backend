package com.novelbot.api.dto;

import lombok.Data;

@Data
public class ChatroomDTO {
    private Long chat_id;
    private Long user_id;
    private Long novel_id;
    private String chat_title;
    private String created_at;
}
