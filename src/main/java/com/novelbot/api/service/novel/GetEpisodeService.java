package com.novelbot.api.service.novel;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.repository.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetEpisodeService {
    @Autowired
    private EpisodeRepository episodeRepository;

    public Optional<Episode> fetchEpisodeContent(int novel_id, int episode_number) {
        if(novel_id <= 0) {
            throw new IllegalArgumentException("novel_id 값이 음수입니다.");
        }

        if(episode_number <= 0) {
            throw new IllegalArgumentException("episode 회차가 올바르지 않습니다.");
        }

        Optional<Episode> episode = episodeRepository.findEpisodeByNovelId(novel_id, episode_number);

        return episode;
    }
}

