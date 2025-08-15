package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.domain.Purchase;
import com.novelbot.api.domain.Queries;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.chat.QueryDto;
import com.novelbot.api.mapper.chat.QueryDtoMapper;
import com.novelbot.api.repository.ChatRepository;
import com.novelbot.api.repository.QueryRepository;
import com.novelbot.api.repository.PurchaseRepository;
import com.novelbot.api.config.JwtTokenValidator;

import com.novelbot.api.repository.UserRepository;
import com.novelbot.api.service.API.APIService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final APIService apiService;
    private final QueryRepository queryRepository;
    private final ChatRepository chatRepository;
    private final PurchaseRepository purchaseRepository;
    private final QueryDtoMapper queryDtoMapper;
    private final JwtTokenValidator jwtTokenValidator;
    private final UserRepository userRepository;

    public QueryService(APIService apiService, QueryRepository queryRepository, ChatRepository chatRepository,
                        QueryDtoMapper queryDtoMapper, PurchaseRepository purchaseRepository, JwtTokenValidator jwtTokenValidator, UserRepository userRepository) {
        this.apiService = apiService;
        this.queryRepository = queryRepository;
        this.chatRepository = chatRepository;
        this.purchaseRepository = purchaseRepository;
        this.queryDtoMapper = queryDtoMapper;
        this.jwtTokenValidator = jwtTokenValidator;
        this.userRepository = userRepository;
    }

    /**
     * 새로운 질문 생성
     */
    @Transactional
    public QueryDto createQuery(Integer chatId, Integer novelId, String queryContent, String token) {
        if(token == null || token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token이 올바르지 않은 형태입니다.");
        }
        if (chatId == null || queryContent == null || queryContent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        Chatroom chatroom = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        Queries query = new Queries(queryContent, null, chatroom);
        Queries savedQuery = queryRepository.save(query);

        Integer userId = jwtTokenValidator.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        List<Purchase> purchaseList = purchaseRepository.findByUser(user);
        List<Integer> episodeIds = List.of();

        int index = 0;
        while(!purchaseList.isEmpty()) {
            if(purchaseList.get(index).getNovel().getId().equals(novelId)) {
                episodeIds.set(index, purchaseList.get(index).getEpisode().getId());
                index++;
            }
        }

        return apiService.chat(queryContent, episodeIds)
                .map(answer -> {
                    savedQuery.updateAnswer(answer);
                    queryRepository.save(savedQuery);
                    return queryDtoMapper.toDto(savedQuery);
                })
                .onErrorMap(ex -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "AI 답변 생성 실패: " + ex.getMessage())).block();

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
