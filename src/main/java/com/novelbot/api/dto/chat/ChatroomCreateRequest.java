package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomCreateRequest {
    private Long userId;
    private Long novelId;
    private String chatTitle;
}
