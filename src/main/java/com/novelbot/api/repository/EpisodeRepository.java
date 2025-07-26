package com.novelbot.api.repository;

import com.novelbot.api.domain.Episode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    List<Episode> findByNovelNovelId(int novelId);
}