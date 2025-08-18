package com.novelbot.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "episode_query")
public class QueryEpisode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_episode_id", nullable = false)
    private Integer id;

    @Column(name = "episode_number")
    private Integer episodeNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_id", nullable = false)
    private Queries queries;
    
    public QueryEpisode(Episode episode, Queries queries) {
        this.episode = episode;
        this.queries = queries;
    }
}