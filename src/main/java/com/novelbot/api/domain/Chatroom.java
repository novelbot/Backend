package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "chatroom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Integer id;

    @Column(name = "chat_title")
    private String chatTitle;

    @Column(name = "created_at", nullable = false)
    private java.sql.Timestamp createdAt;

    // 사용자 - 채팅방 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    // 소설 - 채팅방 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", referencedColumnName = "novel_id", nullable = false)
    private Novel novel;

    // 에피소드 - 채팅방 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", referencedColumnName = "episode_id", nullable = false)
    private Episode episode;

    // 채팅방 - 쿼리 일대다 매칭 카디널리티
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Queries> queries = new ArrayList<>();

    public Chatroom(String chatTitle, User user, Novel novel, Episode episode) {
        this.chatTitle = (chatTitle != null) ? chatTitle : "기본 채팅방";
        this.user = user;
        this.novel = novel;
        this.episode = episode;
        this.createdAt = new java.sql.Timestamp(System.currentTimeMillis());
        this.queries = new ArrayList<>();
    }
}
