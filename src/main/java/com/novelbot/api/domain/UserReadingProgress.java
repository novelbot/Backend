package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "USER_READING_PROCESS")
public class UserReadingProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id", nullable = false)
    private Long progressId;

    // 에피소드 - 독서 진도 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", referencedColumnName = "episode_id", nullable = false)
    private Episode episode;

    // 사용자 - 독서 진도 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    // 소설 - 진도 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", referencedColumnName = "novel_id", nullable = false)
    private Novel novel;

    @Column(name = "last_read_page")
    private int lastReadPage;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp updatedAt;
}

