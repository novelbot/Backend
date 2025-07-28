package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryCreateRequest;
import com.novelbot.api.dto.chat.QueryRequest;
import com.novelbot.api.dto.chat.QueryResponse;
import com.novelbot.api.mapper.chat.QueryCreateRequestDtoMapper;
import com.novelbot.api.mapper.chat.QueryRequestDtoMapper;
import com.novelbot.api.mapper.chat.QueryResponseDtoMapper;

import com.novelbot.api.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QueryService {
    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private QueryCreateRequestDtoMapper queryCreateRequestDtoMapper;

    public void createQuery(QueryCreateRequest queryCreateRequest) {
        if(queryCreateRequest == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(올바르지 않는 형식의 파라미터 값들 입니다.)"
            );
        }
        if(queryCreateRequest.getQueryContent() == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(질문 내용이 비어있습니다.)"
            );
        }
        if(queryCreateRequest.getPageNumber() <= 0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(올바르지 않은 페이지 번호입니다.)"
            );
        }



    }
}
