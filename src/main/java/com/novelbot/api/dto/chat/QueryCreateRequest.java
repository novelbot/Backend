package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryCreateRequest {
    private String queryContent;
    private int pageNumber;
    private int chat_id;
}
