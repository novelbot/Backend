package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long queryId;
    private String queryAnswer;
}
