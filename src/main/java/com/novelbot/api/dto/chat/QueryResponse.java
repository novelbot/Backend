package com.novelbot.api.dto.chat;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
    private String queryAnswer;
    // OpenAI API의 토큰 사용량
    private List<Integer> tokens;
    Integer pageNumber;
    Integer chatId;
}

// 반환 형식
// {
//  "query": "string",
//  “tokens”: [],
//  “chat_id”: int,
//}
//
//
//{
//  “answer”: {},
//  “reference_episodes”: [],
//  “chat_id”: int
//}