package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomCreateRequest {
    private Integer novelId;
    private Integer episodeId;
    private String chatTitle;
}
