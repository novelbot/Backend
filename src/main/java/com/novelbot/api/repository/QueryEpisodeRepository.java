package com.novelbot.api.repository;

import com.novelbot.api.domain.QueryEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QueryEpisodeRepository extends JpaRepository<QueryEpisode, Integer> {
    @Query("SELECT qe.episode.id FROM QueryEpisode qe WHERE qe.queries.id = :queryId")
    List<Integer> findEpisodeIdsByQueryId(@Param("queryId") Integer queryId);
}