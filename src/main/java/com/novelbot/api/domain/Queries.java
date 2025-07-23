package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "QUERIES")
public class Queries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_id", nullable = false)
    private Long query_id;

    @Column(name = "query_content")
    private String query_content;

    @Column(name = "query_answer", nullable = false)
    private String query_answer;

    @Column(name = "asked_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp asked_at;

    @Column(name = "page_number")
    private int page_number;

    @Column(name = "LLM_id", nullable = false)
    private Long LLM_id;

    // 사용자 - 쿼리 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    private Chatroom chatroom;

}
