package com.novelbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long user_id;

    @Column(name = "user_name", nullable = false)
    private String user_name;

    @Column(name = "user_password", nullable = false)
    private String user_password;

    @Column(name = "user_nickname")
    private String user_nickname;

    @Column(name = "user_email", nullable = false)
    private String user_email;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp created_at;

    // 구매 - 사용자 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

    // 사용자 - 독서 진도 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User_reading_process> user_reading_processes = new ArrayList<>();

    // 사용자 - 채팅방 진도 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chatroom> chatrooms = new ArrayList<>();

}
