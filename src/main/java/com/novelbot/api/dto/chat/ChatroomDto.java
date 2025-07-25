package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomDto {
    private int chatId;
    private int userId;
    private int novelId;
    private String chatTitle;
    private String createdAt;
}
