package com.novelbot.api.mapper.chat;

import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryDto;
import org.springframework.stereotype.Component;

@Component
public class QueryDtoMapper {
    public QueryDto toDto(Queries queries) {
        QueryDto queryDto = new QueryDto();

        queryDto.setQueryId(queries.getId());
        queryDto.setChatId(queries.getChatRoom().getId());
        queryDto.setUserId(queries.getChatRoom().getUser().getId());
        queryDto.setNovelId(queries.getChatRoom().getNovel().getId());
        queryDto.setQueryContent(queries.getQueryContent());
        queryDto.setQueryAnswer(queries.getQueryAnswer());
        queryDto.setAskedAt(queries.getAskedAt().toString());
        queryDto.setPageNumber(queries.getPageNumber());
        queryDto.setField(queries.getField());

        return queryDto;
    }
}
