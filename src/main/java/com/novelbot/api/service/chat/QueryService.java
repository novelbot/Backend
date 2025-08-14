package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryDto;
import com.novelbot.api.mapper.chat.QueryDtoMapper;
import com.novelbot.api.repository.ChatRepository;
import com.novelbot.api.repository.QueryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final QueryRepository queryRepository;
    private final ChatRepository chatRepository;
    private final QueryDtoMapper queryDtoMapper;

    public QueryService(QueryRepository queryRepository, ChatRepository chatRepository,
            QueryDtoMapper queryDtoMapper) {
        this.queryRepository = queryRepository;
        this.chatRepository = chatRepository;
        this.queryDtoMapper = queryDtoMapper;
    }

    /**
     * 새로운 질문 생성
     */
    @Transactional
    public Integer createQuery(Integer chatId, String queryContent) {
        if (chatId == null || queryContent == null || queryContent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        Chatroom chatroom = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        Queries query = new Queries(queryContent, null, chatroom);
        Queries savedQuery = queryRepository.save(query);
        return savedQuery.getId();
    }

    /**
     * 질문 목록 조회
     */
    @Transactional(readOnly = true)
    public List<QueryDto> getQueriesByChatId(Integer chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다.");
        }

        return queryRepository.findByChatRoomId(chatId).stream()
                .map(queryDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 질문에 대한 답변 생성 및 저장
     */
    @Transactional
    public QueryDto generateAnswer(Integer queryId) {
        Queries query = queryRepository.findById(queryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."));

        // TODO: 실제 AI 모델을 연동하여 답변 생성
        String generatedAnswer = "AI가 생성한 답변입니다: '" + query.getQueryContent() + "'에 대한 답변.";

        query.updateAnswer(generatedAnswer);
        queryRepository.save(query);

        return queryDtoMapper.toDto(query);
    }

    /**
     * 질문 삭제
     */
    @Transactional
    public void deleteQuery(Integer queryId) {
        if (!queryRepository.existsById(queryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 질문을 찾을 수 없습니다.");
        }
        queryRepository.deleteById(queryId);
    }
}
