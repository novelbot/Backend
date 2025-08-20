package com.novelbot.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.novelbot.api.dto.API.QueryAsk;
import com.novelbot.api.service.API.APIService;

import reactor.core.publisher.Mono;

@RestController
public class APIController {
    private final APIService apiService;

    public APIController(APIService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/api/v1/auth/login")
    public Mono<ResponseEntity<String>> login(@RequestBody Map<String, Object> loginRequest) {
        String username = (String) loginRequest.get("username");
        String password = (String) loginRequest.get("password");
        Boolean rememberMe = (Boolean) loginRequest.getOrDefault("remember_me", false);

        // 기본 인증 정보 검증 (admin/admin123)
        if (!"admin".equals(username) || !"admin123".equals(password)) {
            return Mono.just(ResponseEntity.status(401).body("Invalid username or password"));
        }

        return apiService.login(username, password, rememberMe)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(500).body(ex.getMessage())));
    }

    @PostMapping("/api/v1/episode/chat/stream")
    public Mono<ResponseEntity<String>> chat(@RequestBody Map<String, Object> chatRequest) {
        String message = (String) chatRequest.get("message");
        List<?> episodeIdsRaw = (List<?>) chatRequest.get("episode_ids");
        List<Integer> episodeIds = episodeIdsRaw != null
            ? episodeIdsRaw.stream().map(e -> Integer.valueOf(e.toString())).toList()
            : null;
        String conversationId = (String) chatRequest.get("conversation_id");
        Boolean useConversationContext = (Boolean) chatRequest.getOrDefault("use_conversation_context", false);

        // 필수 필드 검증
        if (message == null || episodeIds == null) {
            return Mono.just(ResponseEntity.badRequest().body("Message and episode_ids are required"));
        }

        // QueryAsk 객체 생성
        QueryAsk queryAsk = new QueryAsk();
        queryAsk.setQueryContent(message);
        queryAsk.setIsBoughtEpisodes(episodeIds.toArray(new Integer[0]));
        queryAsk.setUsingContext(false);
        
        return apiService.chat(queryAsk)
                .map(response -> ResponseEntity.ok(response.getAnswerContent()))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(500).body(ex.getMessage())));
    }
}
