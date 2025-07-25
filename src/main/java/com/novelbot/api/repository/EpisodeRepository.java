package com.novelbot.api.repository;

import com.novelbot.api.domain.Episode;

import java.util.List;
import java.util.Optional;

import com.novelbot.api.domain.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findByNovelNovelId(Long novelId);

    Optional<Episode> findEpisodeByNovelId(Long novel_id, int episode_number);

    Long novel(Novel novel);
}