package com.novelbot.api.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Queries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
