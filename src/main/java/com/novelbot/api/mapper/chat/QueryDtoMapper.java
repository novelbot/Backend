package com.novelbot.api.mapper.chat;

import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryDto;
import com.novelbot.api.repository.QueryEpisodeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryDtoMapper {

    private final QueryEpisodeRepository queryEpisodeRepository;

    public QueryDtoMapper(QueryEpisodeRepository queryEpisodeRepository) {
        this.queryEpisodeRepository = queryEpisodeRepository;
    }

    public QueryDto toDto(Queries queries) {
        QueryDto queryDto = new QueryDto();

        queryDto.setQueryId(queries.getId());
        queryDto.setChatId(queries.getChatRoom().getId());
        queryDto.setUserId(queries.getChatRoom().getUser().getId());
        queryDto.setNovelId(queries.getChatRoom().getNovel().getId());
        List<Integer> episodeIds = queryEpisodeRepository.findEpisodeIdsByQueryId(queries.getId());
        queryDto.setEpisodeIds(episodeIds);
        queryDto.setQueryContent(queries.getQueryContent());
        queryDto.setQueryAnswer(queries.getQueryAnswer());
        queryDto.setAskedAt(queries.getAskedAt().toString());
        queryDto.setPageNumber(queries.getPageNumber());
        queryDto.setField(queries.getField());

        return queryDto;
    }
}
