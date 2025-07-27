package com.novelbot.api.mapper.novel;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.dto.novel.EpisodeDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EpisodeDtoMapper {
    public EpisodeDto toDto(Optional<Episode> optionalepisode){
        if(optionalepisode.isEmpty()){
            return null;
        }

        Episode episode = optionalepisode.get();
        EpisodeDto episodeDto = new EpisodeDto();

        episodeDto.setEpisodeId(episode.getEpisodeId());
        episodeDto.setNovelId(episode.getNovel().getNovelId());
        episodeDto.setEpisodeNumber(episode.getEpisodeNumber());
        episodeDto.setEpisodeTitle(episode.getEpisodeTitle());
        episodeDto.setContent(episode.getContent());
        episodeDto.setPublicationDate(String.valueOf(episode.getPublicationDate()));

        return episodeDto;
    }
}
