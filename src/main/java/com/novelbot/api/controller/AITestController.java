package com.novelbot.api.controller;

import com.novelbot.api.dto.API.QueryAsk;
import com.novelbot.api.dto.API.QueryAnswerResponse;
import com.novelbot.api.service.API.APIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/test")
@Tag(name = "AI Server Test", description = "AI 서버 연동 테스트 API")
public class AITestController {

    private final APIService apiService;

    public AITestController(APIService apiService) {
        this.apiService = apiService;
    }

    @Operation(summary = "AI 서버 로그인 테스트", description = "AI 서버에 로그인하여 JWT 토큰을 받아오는 테스트")
    @PostMapping("/login")
    public ResponseEntity<Mono<String>> testLogin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(defaultValue = "false") boolean rememberMe) {
        
        Mono<String> result = apiService.login(username, password, rememberMe);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "AI 서버 채팅 테스트", description = "AI 서버에 질문을 보내고 답변을 받아오는 테스트")
    @PostMapping("/chat")
    public ResponseEntity<Mono<QueryAnswerResponse>> testChat(@RequestBody QueryAsk queryAsk) {
        Mono<QueryAnswerResponse> result = apiService.chat(queryAsk);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "간단한 채팅 테스트", description = "기본 파라미터로 AI 서버 채팅 테스트")
    @PostMapping("/chat-simple")
    public ResponseEntity<Mono<QueryAnswerResponse>> testChatSimple(
            @RequestParam String message,
            @RequestParam(required = false) Integer[] episodeIds) {
        
        QueryAsk queryAsk = new QueryAsk();
        queryAsk.setQueryContent(message);
        queryAsk.setIsBoughtEpisodes(episodeIds != null ? episodeIds : new Integer[]{});
        queryAsk.setUsingContext(false);
        
        Mono<QueryAnswerResponse> result = apiService.chat(queryAsk);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "전체 플로우 테스트", description = "로그인 → 채팅 → 응답 확인하는 전체 플로우 테스트")
    @PostMapping("/full-flow")
    public ResponseEntity<Mono<String>> testFullFlow(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String message,
            @RequestParam(required = false) Integer[] episodeIds) {
        
        Mono<String> result = apiService.login(username, password, false)
                .flatMap(token -> {
                    QueryAsk queryAsk = new QueryAsk();
                    queryAsk.setQueryContent(message);
                    queryAsk.setIsBoughtEpisodes(episodeIds != null ? episodeIds : new Integer[]{});
                    queryAsk.setUsingContext(false);
                    
                    return apiService.chat(queryAsk);
                })
                .map(response -> "Success! ConversationId: " + response.getConversationId() + 
                               ", Answer: " + response.getAnswerContent() +
                               ", Referenced Episodes: " + java.util.Arrays.toString(response.getReferencedEpisodes()));
        
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "AI 서버 연결 상태 확인", description = "AI 서버가 살아있는지 확인")
    @GetMapping("/ping")
    public ResponseEntity<Mono<String>> testPing() {
        Mono<String> result = apiService.getAPI();
        return ResponseEntity.ok(result);
    }
}