package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "chatroom")
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Integer id;

    @Column(name = "chat_title")
    private String chatTitle;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp createdAt;

    // 사용자 - 채팅방 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    // 소설 - 채팅방 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", referencedColumnName = "novel_id", nullable = false)
    private Novel novel;

    // 채팅방 - 쿼리 일대다 매칭 카디널리티
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Queries> queries = new ArrayList<>();

    public Chatroom(String chatTitle, Optional<User> user, Optional<Novel> novel) {}

    public Chatroom(String chatTitle, User user, Novel novel) {
        this.chatTitle = chatTitle;
        this.user = user;
        this.novel = novel;
        this.createdAt = new java.sql.Timestamp(System.currentTimeMillis());
        this.queries = new ArrayList<>();
    }
}
