package com.novelbot.api.repository;

import com.novelbot.api.domain.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    
    List<Episode> findAllByNovelId(int novelId);
    Optional<Episode> findByNovelIdAndEpisodeNumber(int novelId, int episodeNumber);
}