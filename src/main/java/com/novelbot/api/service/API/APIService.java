package com.novelbot.api.service.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.novelbot.api.dto.API.QueryAsk;
import com.novelbot.api.dto.API.QueryAnswerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class APIService {
    @Autowired
    private WebClient webClient;
    
    @Value("${ai.server.url}")
    private String aiServerUrl;
    
    @Value("${ai.server.username}")
    private String aiUsername;
    
    @Value("${ai.server.password}")
    private String aiPassword;
    
    private String jwtToken; // JWT 토큰 저장
    private String refreshToken; // Refresh 토큰 저장

    // GET 요청
    public Mono<String> getAPI(){
        return webClient.get()
                .uri(aiServerUrl + "/")
                .retrieve()
                .bodyToMono(String.class);
    }

    // 로그인 요청(POST) - JWT 토큰 획득 및 저장
    public Mono<String> login(String username, String password, boolean rememberMe) {
        return webClient.post()
                .uri(aiServerUrl + "/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "username", username,
                        "password", password,
                        "remember_me", rememberMe
                ))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = mapper.readTree(response);
                        String accessToken = jsonNode.get("access_token").asText();
                        String refreshTokenValue = jsonNode.has("refresh_token") ? jsonNode.get("refresh_token").asText() : null;
                        this.refreshToken = refreshTokenValue; // Refresh 토큰 저장
                        return accessToken;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse login response: " + e.getMessage());
                    }
                })
                .doOnNext(token -> this.jwtToken = token) // JWT 토큰 저장
                .onErrorMap(ex -> new RuntimeException("Login failed: " + ex.getMessage()));
    }
    
    // Refresh Token으로 토큰 재발급
    public Mono<String> refreshAccessToken() {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Mono.error(new RuntimeException("No refresh token available"));
        }
        
        return webClient.post()
                .uri(aiServerUrl + "/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = mapper.readTree(response);
                        String accessToken = jsonNode.get("access_token").asText();
                        String refreshTokenValue = jsonNode.has("refresh_token") ? jsonNode.get("refresh_token").asText() : this.refreshToken;
                        this.refreshToken = refreshTokenValue; // Refresh 토큰 업데이트
                        return accessToken;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse refresh response: " + e.getMessage());
                    }
                })
                .doOnNext(token -> this.jwtToken = token) // JWT 토큰 업데이트
                .onErrorMap(ex -> new RuntimeException("Token refresh failed: " + ex.getMessage()));
    }
    
    // JWT 토큰으로 자동 로그인
    public Mono<String> ensureAuthenticated() {
        if (jwtToken != null && !jwtToken.isEmpty()) {
            return Mono.just(jwtToken);
        }
        // 환경 변수에서 로그인 정보 사용
        return login(aiUsername, aiPassword, false);
    }

    // 대화 요청(POST) - QueryAsk 사용, JWT 인증 포함, QueryAnswerResponse 반환
    public Mono<QueryAnswerResponse> chat(QueryAsk queryAsk) {
        return ensureAuthenticated()
                .<QueryAnswerResponse>flatMap(token -> webClient.post()
                        .uri(aiServerUrl + "/api/v1/episode/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .bodyValue(queryAsk)
                        .retrieve()
                        .bodyToMono(QueryAnswerResponse.class))
                .onErrorResume(ex -> {
                    // 401 에러 시 토큰 재발급 후 재시도
                    if (ex.getMessage() != null && ex.getMessage().contains("401")) {
                        this.jwtToken = null; // 기존 토큰 초기화
                        
                        // 1차: Refresh token으로 토큰 재발급 시도
                        return refreshAccessToken()
                                .onErrorResume(refreshEx -> {
                                    // 2차: Refresh token 실패 시 일반 로그인으로 fallback
                                    this.refreshToken = null; // Refresh 토큰도 초기화
                                    return login(aiUsername, aiPassword, false);
                                })
                                .flatMap(newAccessToken -> {
                                    // 새로운 토큰으로 원래 요청 재시도
                                    return webClient.post()
                                            .uri(aiServerUrl + "/api/v1/episode/chat")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .header("Authorization", "Bearer " + newAccessToken)
                                            .bodyValue(queryAsk)
                                            .retrieve()
                                            .bodyToMono(QueryAnswerResponse.class);
                                });
                    }
                    return Mono.error(new RuntimeException("Chat API call failed: " + ex.getMessage()));
                });
    }


    // DELETE 요청 방식
    public Mono<String> deleteAPI(){
        return webClient.delete()
                .uri(aiServerUrl + "/", 1)
                .retrieve()
                .bodyToMono(String.class);
    }

    // PATCH 요청 방식
    public Mono<String> patchAPI(){
        return webClient.patch()
                .uri(aiServerUrl + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("title", "patched title"))
                .retrieve()
                .bodyToMono(String.class);
    }
}
