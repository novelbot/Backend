package com.novelbot.api.dto;

import lombok.Data;

@Data
public class QueriesDTO {
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
