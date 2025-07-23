package com.novelbot.dto;

import lombok.Data;

@Data
public class QueriesDTO {
    private Long query_id;
    private Long chat_id;
    private Long user_id;
    private Long novel_id;
    private String query_content;
    private String query_answer;
    private String asked_at;
    private int page_number;
    private Long LLM_id;
}
