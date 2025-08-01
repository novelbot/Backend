package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryDto {
    private Integer queryId;
    private Integer chatId;
    private Integer userId;
    private Integer novelId;
    private String queryContent;
    private String queryAnswer;
    private String askedAt;
    private Integer pageNumber;
    private String field;
}
