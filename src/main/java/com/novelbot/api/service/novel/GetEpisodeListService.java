package com.novelbot.api.service.novel;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.repository.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GetEpisodeListService {
    @Autowired
    private EpisodeRepository episodeRepository;
    
    public List<Episode> findEpisodesByNovelId(int novelId) throws IOException {
        return episodeRepository.findByNovelNovelId(novelId);
    }
}