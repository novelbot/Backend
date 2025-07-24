package com.novelbot.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ChatroomDTO {
    private Long chatId;
    private Long userId;
    private Long novelId;
    private String chatTitle;
    private String createdAt;
}
