package com.novelbot.api.mapper.chat;

import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class QueryCreateRequestDtoMapper {
    public QueryCreateRequest toDto(QueryCreateRequest queryCreateRequest) {
        QueryCreateRequest queryCreateRequestDto = new QueryCreateRequest();

        queryCreateRequestDto.setQueryContent(queryCreateRequest.getQueryContent());
        queryCreateRequestDto.setPageNumber(queryCreateRequest.getPageNumber());
        queryCreateRequestDto.setChatId(queryCreateRequest.getChatId());

        return queryCreateRequestDto;
    }
}
