package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "EPISODE")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "episode_id", nullable = false)
    private Long episode_id;

    @Column(name = "episode_title", nullable = false)
    private String episode_title;

    @Column(name = "episode_number", nullable = false)
    private int episode_number;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "publication_date", nullable = true)
    private java.sql.Date publication_date;

    // 소설 - 에피소드 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", referencedColumnName = "novel_id", nullable = false)
    private Novel novel;

    // 에피소드 - 독서 진도 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User_reading_process> user_reading_processes = new ArrayList<>();

    // 에피소드 - 구매 일대다 매핑 카디널리티
    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases = new ArrayList<>();

}
