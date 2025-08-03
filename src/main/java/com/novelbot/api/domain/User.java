package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.*;

@Entity
@Getter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp createdAt;

    @Column(name = "user_role", nullable = false)

    // 구매 - 사용자 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

    // 사용자 - 독서 진도 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserReadingProgress> userReadingProcesses = new ArrayList<>();

    // 사용자 - 채팅방 진도 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chatroom> chatrooms = new ArrayList<>();

    public User() {}

    public User(String userName, String userPassword, String userNickname, String userEmail){
        this.userName = userName;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userEmail = userEmail;
        this.createdAt = new java.sql.Timestamp(System.currentTimeMillis());
    }
}
