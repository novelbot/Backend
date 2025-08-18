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

    public QueryService(APIService apiService, QueryRepository queryRepository, ChatRepository chatRepository,
                        QueryDtoMapper queryDtoMapper, PurchaseRepository purchaseRepository, 
                        QueryEpisodeRepository queryEpisodeRepository, EpisodeRepository episodeRepository,
                        JwtTokenValidator jwtTokenValidator, UserRepository userRepository) {
        this.apiService = apiService;
        this.queryRepository = queryRepository;
        this.chatRepository = chatRepository;
        this.purchaseRepository = purchaseRepository;
        this.queryEpisodeRepository = queryEpisodeRepository;
        this.episodeRepository = episodeRepository;
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
        if (queryContent.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "쿼리 내용은 255자를 초과할 수 없습니다.");
        }

        Chatroom chatroom = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."));

        Queries query = new Queries(queryContent, null, chatroom);
        Queries savedQuery = queryRepository.save(query);

        Integer userId = jwtTokenValidator.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        // 구매한 에피소드 ID 수집
        List<Purchase> purchaseList = purchaseRepository.findByUser(user);
        Integer[] isBoughtEpisodes = purchaseList.stream()
                .filter(purchase -> purchase.getNovel().getId().equals(novelId))
                .map(purchase -> purchase.getEpisode().getId())
                .toArray(Integer[]::new);

        // QueryAsk DTO 생성
        QueryAsk queryAsk = new QueryAsk();
        queryAsk.setQueryContent(queryContent);
        queryAsk.setIsBoughtEpisodes(isBoughtEpisodes);

        return apiService.chat(queryAsk)
                .map(response -> {
                    // Queries 엔티티에 답변과 conversationId 저장
                    savedQuery.updateAnswer(response.getAnswerContent());
                    savedQuery.setField(response.getConversationId());
                    queryRepository.save(savedQuery);
                    
                    // QueryEpisode 저장 - 참조된 에피소드들
                    if (response.getReferencedEpisodes() != null) {
                        for (Integer episodeId : response.getReferencedEpisodes()) {
                            Episode episode = episodeRepository.findById(episodeId).orElse(null);
                            if (episode != null) {
                                QueryEpisode queryEpisode = new QueryEpisode(episode, savedQuery);
                                queryEpisodeRepository.save(queryEpisode);
                            }
                        }
                    }
                    
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
