package com.novelbot.api.mapper.chat;

import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryResponse;
import org.springframework.stereotype.Component;

@Component
public class QueryResponseDtoMapper {
    public QueryResponse toDto(Queries queries) {
        QueryResponse queryResponse = new QueryResponse();

        queryResponse.setChatId(queries.getChatRoom().getId());
        queryResponse.setQueryAnswer(queries.getQueryAnswer());
        queryResponse.setPageNumber(queries.getPageNumber());

        return queryResponse;
    }
}
