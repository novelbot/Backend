package com.novelbot.api.repository;

import com.novelbot.api.domain.Episode;

import java.util.List;
import java.util.Optional;

import com.novelbot.api.dto.novel.EpisodeDto;
import com.novelbot.api.dto.novel.EpisodeListDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    List<Episode> findByNovelNovelId(int novelId);

    Optional<Episode> findEpisodeByNovelId(int novel_id, int episode_number);
}