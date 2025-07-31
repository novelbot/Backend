package com.novelbot.api.mapper.novel;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.dto.novel.EpisodeListDto;
import org.springframework.stereotype.Component;

@Component
public class EpisodeListDtoMapper {
    public EpisodeListDto toDto(Episode episode){
        EpisodeListDto episodeListDto = new EpisodeListDto();

        episodeListDto.setEpisodeId(episode.getEpisodeId());
        episodeListDto.setNovelId(episode.getNovel().getNovelId());
        episodeListDto.setEpisodeNumber(episode.getEpisodeNumber());
        episodeListDto.setEpisodeTitle(episode.getEpisodeTitle());
        episodeListDto.setPublicationDate(String.valueOf(episode.getPublicationDate()));

        return episodeListDto;
    }
}
