package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryDto {
    private int queryId;
    private int chatId;
    private int userId;
    private int novelId;
    private String queryContent;
    private String queryAnswer;
    private String askedAt;
    private int pageNumber;
    private int field;
}
