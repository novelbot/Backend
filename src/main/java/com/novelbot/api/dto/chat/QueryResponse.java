package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
    private String queryAnswer;
    int pageNumber;
    int chatId;
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