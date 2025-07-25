package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomCreateRequest {
    private int novelId;
    private String chatTitle;
}
