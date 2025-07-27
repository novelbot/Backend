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
    private int userId;

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

    // 구매 - 사용자 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

    // 사용자 - 독서 진도 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserReadingProgress> userReadingProcesses = new ArrayList<>();

    // 사용자 - 채팅방 진도 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chatroom> chatrooms = new ArrayList<>();

    // 회원 가입 시 인스턴스 생성을 위한 메서드
    public void update(String userName, String userNickname, String userPassword, String userEmail){
        this.userName = userName;
        this.userNickname = userNickname;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.createdAt = new java.sql.Timestamp(System.currentTimeMillis());
    }

}
