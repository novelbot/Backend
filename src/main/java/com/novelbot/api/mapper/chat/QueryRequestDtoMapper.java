package com.novelbot.api.mapper.chat;

import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryRequest;
import org.springframework.stereotype.Component;

@Component
public class QueryRequestDtoMapper {
    public QueryRequest toDto(Queries queries) {
        QueryRequest queryRequest = new QueryRequest();

        queryRequest.setChatId(queryRequest.getChatId());
        queryRequest.setQueryContent(queries.getQueryContent());

        return queryRequest;
    }
}
