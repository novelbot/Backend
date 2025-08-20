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
import java.util.concurrent.CompletableFuture;
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

        // AI 서버 호출을 별도 스레드에서 처리 (응답 대기)
        System.out.println("🚀 질문 생성 완료, 비동기 AI 처리 시작 - QueryID: " + savedQuery.getId() + ", ChatID: " + chatId);
        CompletableFuture.runAsync(() -> {
            processAIResponse(savedQuery.getId(), chatId, queryContent, userId);
        });

        return savedQuery.getId();

    }

    /**
     * AI 서버 응답을 처리하고 웹소켓으로 전송
     */
    @Transactional
    public void processAIResponse(Integer queryId, Integer chatId, String queryContent, Integer userId) {
        System.out.println("⚙️ processAIResponse 시작 - QueryID: " + queryId + ", ChatID: " + chatId + ", UserID: " + userId);
        try {
            // 클라이언트가 구독할 시간을 확보하기 위해 잠시 대기
            Thread.sleep(200); // 200ms 대기
            // 사용자와 채팅방 정보 다시 조회
            User user = userRepository.findById(userId).orElse(null);
            Chatroom chatroom = chatRepository.findById(chatId).orElse(null);
            if (user == null || chatroom == null) {
                System.out.println("❌ 사용자 또는 채팅방 정보를 찾을 수 없습니다 - User: " + (user != null) + ", Chatroom: " + (chatroom != null));
                return; // 데이터가 없으면 처리 중단
            }

            // 채팅방에서 소설 ID와 현재 에피소드 정보 조회
            Integer novelId = chatroom.getNovel().getId();
            Integer currentEpisodeId = chatroom.getEpisode().getId();
            Episode currentEpisode = episodeRepository.findById(currentEpisodeId).orElse(null);
            if (currentEpisode == null) {
                return; // 현재 에피소드를 찾을 수 없으면 처리 중단
            }
            Integer currentEpisodeNumber = currentEpisode.getEpisodeNumber();
            
            // 구매한 에피소드 ID 수집 (현재 회차보다 뒤의 에피소드는 제외)
            List<Purchase> purchaseList = purchaseRepository.findByUserWithEpisodeAndNovel(user);
            Integer[] isBoughtEpisodes = purchaseList.stream()
                    .filter(purchase -> purchase.getNovel().getId().equals(novelId))
                    .filter(purchase -> purchase.getIsPurchase()) // 실제로 구매한 것만 필터링
                    .filter(purchase -> purchase.getEpisode().getEpisodeNumber() <= currentEpisodeNumber) // 현재 회차 이하만 포함
                    .map(purchase -> purchase.getEpisode().getId())
                    .toArray(Integer[]::new);

            // QueryAsk DTO 생성
            QueryAsk queryAsk = new QueryAsk();
            queryAsk.setQueryContent(queryContent);
            queryAsk.setIsBoughtEpisodes(isBoughtEpisodes);

            // 구매한 에피소드 ID들 콘솔 출력
            System.out.println("📚 사용자 ID: " + userId + ", 소설 ID: " + novelId);
            System.out.println("📖 현재 질문하고 있는 회차: " + currentEpisodeNumber + "회 (에피소드 ID: " + currentEpisode.getId() + ")");
            System.out.println("📋 AI 서버에 전달할 구매 에피소드 ID들: " + java.util.Arrays.toString(isBoughtEpisodes));
            System.out.println("📝 Query 내용: " + queryContent);

            // AI 서버 호출 (동기적으로 응답 대기)
            System.out.println("🤖 AI 서버에 요청 전송 중... queryId: " + queryId);
            QueryAnswerResponse response = apiService.chat(queryAsk)
                    .doOnError(ex -> {
                        System.out.println("❌ AI 서버 오류: " + ex.getMessage());
                        // 에러 발생 시 질문 상태 업데이트
                        updateQueryWithError(queryId, "답변 생성 중 오류가 발생했습니다.");
                    })
                    .block(); // 완전히 응답을 받을 때까지 대기
            
            System.out.println("✅ AI 서버 응답 완료: " + (response != null ? "성공" : "실패"));

            if (response != null) {
                // 성공적으로 응답 받음
                updateQueryWithResponse(queryId, response);
                
                // WebSocket으로 클라이언트에게 결과 전송
                System.out.println("🔔 WebSocket 메시지 전송: /topic/query/" + queryId);
                System.out.println("📤 전송 데이터: " + response.getAnswerContent());
                try {
                    messagingTemplate.convertAndSend("/topic/query/" + queryId, response);
                    System.out.println("✅ WebSocket 메시지 전송 성공");
                } catch (Exception wsEx) {
                    System.out.println("❌ WebSocket 메시지 전송 실패: " + wsEx.getMessage());
                }
            } else {
                // 응답이 null인 경우
                updateQueryWithError(queryId, "AI 서버로부터 응답을 받지 못했습니다.");
                
                // WebSocket으로 에러 메시지 전송
                QueryAnswerResponse errorResponse = new QueryAnswerResponse();
                errorResponse.setAnswerContent("AI 서버로부터 응답을 받지 못했습니다.");
                try {
                    messagingTemplate.convertAndSend("/topic/query/" + queryId, errorResponse);
                    System.out.println("✅ WebSocket 에러 메시지 전송 성공");
                } catch (Exception wsEx) {
                    System.out.println("❌ WebSocket 에러 메시지 전송 실패: " + wsEx.getMessage());
                }
            }

        } catch (Exception e) {
            // 예외 발생 시 에러 메시지로 업데이트
            System.out.println("💥 processAIResponse 예외 발생: " + e.getMessage());
            e.printStackTrace();
            String errorMessage = "답변 생성 중 오류가 발생했습니다: " + e.getMessage();
            updateQueryWithError(queryId, errorMessage);
            
            // WebSocket으로 에러 메시지 전송
            QueryAnswerResponse errorResponse = new QueryAnswerResponse();
            errorResponse.setAnswerContent(errorMessage);
            try {
                messagingTemplate.convertAndSend("/topic/query/" + queryId, errorResponse);
                System.out.println("✅ WebSocket 예외 에러 메시지 전송 성공");
            } catch (Exception wsEx) {
                System.out.println("❌ WebSocket 예외 에러 메시지 전송 실패: " + wsEx.getMessage());
            }
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
