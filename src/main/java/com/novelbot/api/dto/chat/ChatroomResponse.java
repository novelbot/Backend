package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomResponse {
    private Long chatId;
    private Long userId;
    private Long novelId;
    private String chatTitle;
    private String createdAt;
}
