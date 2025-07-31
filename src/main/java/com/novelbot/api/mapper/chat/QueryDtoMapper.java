package com.novelbot.api.mapper.chat;

import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryDto;
import org.springframework.stereotype.Component;

@Component
public class QueryDtoMapper {
    public QueryDto toDto(Queries queries){
        QueryDto queryDto = new QueryDto();

        queryDto.setQueryId(queries.getId());
        queryDto.setChatId(queryDto.getChatId());
        queryDto.setUserId(queryDto.getUserId());
        queryDto.setNovelId(queryDto.getNovelId());
        queryDto.setQueryContent(queries.getQueryContent());
        queryDto.setQueryAnswer(queries.getQueryAnswer());
        queryDto.setAskedAt(queryDto.getAskedAt());
        queryDto.setPageNumber(queries.getPageNumber());
        queryDto.setField(queries.getField());

        return queryDto;
    }
}
