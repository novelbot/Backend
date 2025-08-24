package com.novelbot.api.domain;

import com.novelbot.api.dto.join.UserCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor
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

    @Column(name = "created_at", nullable = false)
    private java.sql.Timestamp createdAt;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    // 구매 - 사용자 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

    // 사용자 - 독서 진도 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserReadingProgress> userReadingProcesses = new ArrayList<>();

    // 사용자 - 채팅방 진도 관계 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chatroom> chatrooms = new ArrayList<>();

    protected User(String userName, String userPassword, String userNickname, String userEmail, String userRole){
        this.userName = userName;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userEmail = userEmail;
        this.createdAt = new java.sql.Timestamp(System.currentTimeMillis());
        this.userRole = (userRole != null) ? userRole : "USER";
    }

    public static User create(UserCreateRequest request, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(request.getUserPassword().trim());
        return new User(request.getUserName(), encodedPassword, request.getUserNickname(), request.getUserEmail(), "USER");
    }

    public void updateUserInfo(String userName, String userPassword, String userNickname, String userEmail, PasswordEncoder passwordEncoder) {
        if (userName != null && !userName.trim().isEmpty()) {
            this.userName = userName.trim();
        }
        if (userPassword != null && !userPassword.trim().isEmpty()) {
            this.userPassword = passwordEncoder.encode(userPassword.trim());
        }
        if (userNickname != null) {
            this.userNickname = userNickname.trim();
        }
        if (userEmail != null && !userEmail.trim().isEmpty()) {
            this.userEmail = userEmail.trim();
        }
    }
}
