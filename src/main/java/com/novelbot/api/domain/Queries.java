package com.novelbot.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "queries")
public class Queries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_id", nullable = false)
    private Integer id;

    @Column(name = "query_content", length = 255)
    @Size(max = 255, message = "쿼리 내용은 255자를 초과할 수 없습니다.")
    private String queryContent;

    @Column(name = "query_answer", nullable = true, length = 255)
    @Size(max = 255, message = "쿼리 답변은 255자를 초과할 수 없습니다.")
    private String queryAnswer;

    @Column(name = "asked_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp askedAt;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Column(name = "LLM_id", nullable = true, length = 255)
    @Size(max = 255, message = "LLM ID는 255자를 초과할 수 없습니다.")
    private String field;

    // 사용자 - 쿼리 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    private Chatroom chatRoom;

    public Queries() {}

    public Queries(String queryContent, Integer pageNumber, Chatroom chatRoom) {
        this.queryContent = queryContent;
        this.pageNumber = pageNumber;
        this.chatRoom = chatRoom;
        this.askedAt = new java.sql.Timestamp(System.currentTimeMillis());
    }

    public void updateAnswer(String answer) {
        this.queryAnswer = answer;
    }
}
