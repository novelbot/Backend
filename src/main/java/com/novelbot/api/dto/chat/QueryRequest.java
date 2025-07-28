package com.novelbot.api.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {
    private int chatId;
    private String queryContent;
}

// 요청 형식
// curl https://api.openai.com/v1/chat/completions \
//-H "Content-Type: application/json" \
//-H "Authorization: Bearer $OPENAI_API_KEY" \
//-d '{
//      "model": "gpt-3.5-turbo",
//      "messages": [{"role": "user", "content": "TEST!"}],
//      "temperature": 0.7
//    }'
//{
//  "id": "chatcmpl-ByEKsperQ568D8u3LXuUwPR6kL7DI",
//  "object": "chat.completion",
//  "created": 1753694486,
//  "model": "gpt-3.5-turbo-0125",
//  "choices": [
//    {
//      "index": 0,
//      "message": {
//        "role": "assistant",
//        "content": "Hello! How can I assist you today?",
//        "refusal": null,
//        "annotations": []
//      },
//      "logprobs": null,
//      "finish_reason": "stop"
//    }
//  ],
//  "usage": {
//    "prompt_tokens": 9,
//    "completion_tokens": 9,
//    "total_tokens": 18,
//    "prompt_tokens_details": {
//      "cached_tokens": 0,
//      "audio_tokens": 0
//    },
//    "completion_tokens_details": {
//      "reasoning_tokens": 0,
//      "audio_tokens": 0,
//      "accepted_prediction_tokens": 0,
//      "rejected_prediction_tokens": 0
//    }
//  },
//  "service_tier": "default",
//  "system_fingerprint": null
//}