package com.novelbot.api.dto.chat;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryDto {
    private Integer queryId;
    private Integer chatId;
    private Integer userId;
    private Integer novelId;
    private List<Integer> episodeIds;
    private String queryContent;
    private String queryAnswer;
    private String askedAt;
    private Integer pageNumber;
    private Long field;
}
