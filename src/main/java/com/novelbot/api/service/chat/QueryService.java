package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.domain.Episode;
import com.novelbot.api.domain.Purchase;
import com.novelbot.api.domain.Queries;
import com.novelbot.api.domain.QueryEpisode;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.API.QueryAsk;
import com.novelbot.api.dto.API.QueryAnswerResponse;
import com.novelbot.api.dto.chat.QueryDto;
import com.novelbot.api.mapper.chat.QueryDtoMapper;
import com.novelbot.api.repository.ChatRepository;
import com.novelbot.api.repository.EpisodeRepository;
import com.novelbot.api.repository.QueryEpisodeRepository;
import com.novelbot.api.repository.QueryRepository;
import com.novelbot.api.repository.PurchaseRepository;
import com.novelbot.api.config.JwtTokenValidator;

import com.novelbot.api.repository.UserRepository;
import com.novelbot.api.service.API.APIService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
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
    private final QueryEpisodeRepository queryEpisodeRepository;
    private final EpisodeRepository episodeRepository;
    private final QueryDtoMapper queryDtoMapper;
    private final JwtTokenValidator jwtTokenValidator;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public QueryService(APIService apiService, QueryRepository queryRepository, ChatRepository chatRepository,
                        QueryDtoMapper queryDtoMapper, PurchaseRepository purchaseRepository, 
                        QueryEpisodeRepository queryEpisodeRepository, EpisodeRepository episodeRepository,
                        JwtTokenValidator jwtTokenValidator, UserRepository userRepository,
                        SimpMessagingTemplate messagingTemplate) {
        this.apiService = apiService;
        this.queryRepository = queryRepository;
        this.chatRepository = chatRepository;
        this.purchaseRepository = purchaseRepository;
        this.queryEpisodeRepository = queryEpisodeRepository;
        this.episodeRepository = episodeRepository;
        this.queryDtoMapper = queryDtoMapper;
        this.jwtTokenValidator = jwtTokenValidator;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 새로운 질문 생성 (비동기)
     */
    @Transactional
    public Integer createQueryAsync(Integer chatId, String queryContent, String token) {
        if(token == null || token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token이 올바르지 않은 형태입니다.");
        }
        if (chatId == null || queryContent == null || queryContent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
        if (queryContent.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "쿼리 내용은 255자를 초과할 수 없습니다.");
        }

        Chatroom chatroom = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        Integer userId = jwtTokenValidator.getUserId(token);

        // 채팅방 소유권 확인
        if (!chatroom.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 채팅방에 질문을 생성할 권한이 없습니다.");
        }

        // 질문을 먼저 저장 (답변은 나중에 업데이트)
        Queries query = new Queries(queryContent, null, chatroom);
        query.setQueryAnswer("처리중..."); // 기본값 설정
        Queries savedQuery = queryRepository.save(query);

        // AI 서버 호출을 비동기로 처리
        processAIResponse(savedQuery.getId(), chatId, queryContent, userId);

        return savedQuery.getId();

    }

    /**
     * AI 서버 응답을 비동기로 처리
     */
    @Async
    @Transactional
    public void processAIResponse(Integer queryId, Integer chatId, String queryContent, Integer userId) {
        try {
            // 사용자와 채팅방 정보 다시 조회
            User user = userRepository.findById(userId).orElse(null);
            Chatroom chatroom = chatRepository.findById(chatId).orElse(null);
            if (user == null || chatroom == null) {
                return; // 데이터가 없으면 처리 중단
            }

            // 채팅방에서 소설 ID 조회
            Integer novelId = chatroom.getNovel().getId();
            
            // 구매한 에피소드 ID 수집
            List<Purchase> purchaseList = purchaseRepository.findByUser(user);
            Integer[] isBoughtEpisodes = purchaseList.stream()
                    .filter(purchase -> purchase.getNovel().getId().equals(novelId))
                    .filter(purchase -> purchase.getIsPurchase()) // 실제로 구매한 것만 필터링
                    .map(purchase -> purchase.getEpisode().getId())
                    .toArray(Integer[]::new);

            // QueryAsk DTO 생성
            QueryAsk queryAsk = new QueryAsk();
            queryAsk.setQueryContent(queryContent);
            queryAsk.setIsBoughtEpisodes(isBoughtEpisodes);

            // AI 서버 호출
            QueryAnswerResponse response = apiService.chat(queryAsk)
                    .doOnError(ex -> {
                        // 에러 발생 시 질문 상태 업데이트
                        updateQueryWithError(queryId, "답변 생성 중 오류가 발생했습니다.");
                    })
                    .block();

            if (response != null) {
                // 성공적으로 응답 받음
                updateQueryWithResponse(queryId, response);
                
                // WebSocket으로 클라이언트에게 결과 전송
                messagingTemplate.convertAndSend("/topic/chat/" + chatId, response);
            }

        } catch (Exception e) {
            // 예외 발생 시 에러 메시지로 업데이트
            updateQueryWithError(queryId, "답변 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 질문에 AI 응답 업데이트
     */
    @Transactional
    public void updateQueryWithResponse(Integer queryId, QueryAnswerResponse response) {
        Queries query = queryRepository.findById(queryId).orElse(null);
        if (query != null) {
            // 답변과 conversationId 저장
            query.updateAnswer(response.getAnswerContent());
            query.setField(response.getConversationId());
            queryRepository.save(query);
            
            // QueryEpisode 저장 - 참조된 에피소드들
            if (response.getReferencedEpisodes() != null) {
                for (Integer episodeId : response.getReferencedEpisodes()) {
                    Episode episode = episodeRepository.findById(episodeId).orElse(null);
                    if (episode != null) {
                        QueryEpisode queryEpisode = new QueryEpisode(episode, query);
                        queryEpisodeRepository.save(queryEpisode);
                    }
                }
            }
        }
    }

    /**
     * 질문에 에러 메시지 업데이트
     */
    @Transactional
    public void updateQueryWithError(Integer queryId, String errorMessage) {
        Queries query = queryRepository.findById(queryId).orElse(null);
        if (query != null) {
            query.updateAnswer(errorMessage);
            queryRepository.save(query);
        }
    }

    /**
     * 질문 목록 조회
     */
    @Transactional(readOnly = true)
    public List<QueryDto> getQueriesByChatId(Integer chatId, String token) {
        if(token == null || token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token이 올바르지 않은 형태입니다.");
        }

        Integer userId = jwtTokenValidator.getUserId(token);

        Chatroom chatroom = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        // 채팅방 소유자 확인
        if (!chatroom.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 채팅방의 질문을 조회할 권한이 없습니다.");
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
