package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.domain.Queries;
import com.novelbot.api.dto.chat.QueryCreateRequest;
import com.novelbot.api.dto.chat.QueryRequest;
import com.novelbot.api.dto.chat.QueryResponse;
import com.novelbot.api.mapper.chat.QueryCreateRequestDtoMapper;
import com.novelbot.api.mapper.chat.QueryRequestDtoMapper;
import com.novelbot.api.mapper.chat.QueryResponseDtoMapper;
import com.novelbot.api.repository.ChatRepository;
import com.novelbot.api.repository.QueryEpisodeRepository;
import com.novelbot.api.repository.QueryRepository;

import com.novelbot.api.utility.openAI.OpenAiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QueryService {
        @Autowired
        private QueryRepository queryRepository;

        @Autowired
        private ChatRepository chatRepository;

        @Autowired
        private QueryEpisodeRepository queryEpisodeRepository;

        @Autowired
        private QueryCreateRequestDtoMapper queryCreateRequestDtoMapper;

        @Value("${jwt.secret}")
        private String jwtSecret;

        @Value("${openai.api.key}")
        private String openaiApiKey;

        private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public QueryResponse createQuery(QueryCreateRequest queryCreateRequest, String token) {
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
        if (token == null || token.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(토큰이 비어 있습니다)"
            );
        }

        String username;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            username = claims.getSubject();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Error Code: 401, Unauthorized(유효하지 않은 토큰입니다: " + e.getMessage() + ")"
            );
        }

        Optional<Chatroom> chatroom = Optional.ofNullable(chatRepository.findById(queryCreateRequest.getChat_id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(존재하지 않는 채팅방입니다)"
                )));

        OpenAiResponse openAiResponse = callOpenAiApi(queryCreateRequest.getQueryContent());

        Queries query(
                queryCreateRequest.getQueryContent(), null,
                0, chatroom);

        Queries savedQuery;
        try {
            savedQuery = queryRepository.save(query);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error Code: 500, Internal Server Error(질문 저장 중 오류 발생: " + e.getMessage() + ")"
            );
        }

        List<Integer> referencedEpisodes = queryEpisodeRepository.findByQueriesQueryId(savedQuery.getQueryId())
                .stream()
                .map(QueryEpiosode::getEpisodeNumber)
                .collect(Collectors.toList());

        return new QueryResponse(
                savedQuery.getQueryContent(),
                Arrays.asList(openAiResponse.getPromptTokens(), openAiResponse.getCompletionTokens()),
                savedQuery.getChatRoom().getChatId(),
                savedQuery.getQueryAnswer(),
                referencedEpisodes
        );
    }
}
