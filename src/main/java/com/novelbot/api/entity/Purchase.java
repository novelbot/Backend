package com.novelbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PURCHASE")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id", nullable = false)
    private Long purchase_id;

    // 소설 - 구매 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", referencedColumnName = "novel_id", nullable = false)
    private Novel novel;

    // 에피소드 - 구매 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", referencedColumnName = "episode_id", nullable = false)
    private Episode episode;

    // 사용자 - 독서 진도 일대다 매핑 카디널리티
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "is_purchase", nullable = false)
    private Boolean is_purchase = false;
}