package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
    private Long queryId;
    private Long chatId;
    private Long userId;
    private Long novelId;
    private String queryContent;
    private String queryAnswer;
    private String askedAt;
    private int pageNumber;
    private Long llmId;
}
