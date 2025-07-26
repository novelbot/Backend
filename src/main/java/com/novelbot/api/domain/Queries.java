package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "queries")
public class Queries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_id", nullable = false)
    private int queryId;

    @Column(name = "query_content")
    private String queryContent;

    @Column(name = "query_answer", nullable = false)
    private String queryAnswer;

    @Column(name = "asked_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp askedAt;

    @Column(name = "page_number")
    private int pageNumber;

    @Column(name = "LLM_id", nullable = false)
    private int field;

    // 사용자 - 쿼리 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    private Chatroom chatRoom;

}
