package com.novelbot.api.repository;

import com.novelbot.api.domain.QueryEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueryEpisodeRepository extends JpaRepository<QueryEpisode, Integer> {
    List<Integer> findByQueriesQueryId(int queryId);
}
