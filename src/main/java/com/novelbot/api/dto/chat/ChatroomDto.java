package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomDto {
    private Integer chatId;
    private Integer userId;
    private Integer novelId;
    private String chatTitle;
    private String createdAt;
}
