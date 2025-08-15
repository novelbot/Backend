package com.novelbot.api.controller;

import com.novelbot.api.service.API.APIService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/api/v1/episode/chat")
    public Mono<ResponseEntity<String>> chat(@RequestBody Map<String, Object> chatRequest) {
        String message = (String) chatRequest.get("message");
        List<Integer> episodeIds = (List<Integer>) chatRequest.get("episode_ids");
        String conversationId = (String) chatRequest.get("conversation_id");
        Boolean useConversationContext = (Boolean) chatRequest.getOrDefault("use_conversation_context", false);

        // 필수 필드 검증
        if (message == null || episodeIds == null) {
            return Mono.just(ResponseEntity.badRequest().body("Message and episode_ids are required"));
        }

        return apiService.chat(message, episodeIds)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(500).body(ex.getMessage())));
    }
}
