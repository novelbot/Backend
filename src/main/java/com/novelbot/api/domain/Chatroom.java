package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "CHATROOM")
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Long chat_id;

    @Column(name = "chat_title")
    private String chat_title;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp created_at;

    // 사용자 - 채팅방 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    // 소설 - 채팅방 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", referencedColumnName = "novel_id", nullable = false)
    private Novel novel;

    // 채팅방 - 쿼리 일대다 매칭 카디널리티
    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Queries> queries = new ArrayList<>();

}
