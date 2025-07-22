package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // @CreatedDate 활성화를 위해 추가
@Table(name = "users") // 'USER'는 SQL 예약어일 수 있으므로 'users' 사용을 권장
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 중요: 비밀번호는 반드시 해싱하여 String으로 저장해야 합니다.
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @CreatedDate // 엔티티 생성 시 자동으로 시간 저장
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public User(String password, String name, String nickname, String email) {
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }
}
