package com.novelbot.api.service.fetchingEpisode;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.repository.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EpisodeFetchService {
    @Autowired
    private EpisodeRepository episodeRepository;

    public String fetchEpisodeContent(Long novel_id, int episode_number) {
        if(novel_id == null) {
            throw new IllegalArgumentException("novel_id 값이 비어있습니다.");
        }

        if(episode_number <= 0) {
            throw new IllegalArgumentException("episode 회차가 올바르지 않습니다.");
        }

        Optional<Episode> episode = episodeRepository.findEpisodeByNovelId(novel_id, episode_number);

        return episode.get().getContent();
    }
}

