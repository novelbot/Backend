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
import reactor.core.publisher.Flux;

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
        System.out.println("🔐 AI 서버 로그인 시도: " + aiServerUrl + "/api/v1/auth/login");
        System.out.println("👤 사용자명: " + username);
        
        return webClient.post()
                .uri(aiServerUrl + "/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "username", username,
                        "password", password,
                        "remember_me", rememberMe
                ))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .map(errorBody -> {
                                System.out.println("❌ AI 서버 로그인 실패 - 상태코드: " + clientResponse.statusCode());
                                System.out.println("❌ 에러 응답: " + errorBody);
                                return new RuntimeException("AI 서버 로그인 실패 [" + clientResponse.statusCode() + "]: " + errorBody);
                            });
                })
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        System.out.println("✅ AI 서버 로그인 응답 수신: " + response.length() + "자");
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = mapper.readTree(response);
                        String accessToken = jsonNode.get("access_token").asText();
                        String refreshTokenValue = jsonNode.has("refresh_token") ? jsonNode.get("refresh_token").asText() : null;
                        this.refreshToken = refreshTokenValue; // Refresh 토큰 저장
                        System.out.println("✅ 토큰 저장 완료 - Access Token: " + accessToken.substring(0, Math.min(20, accessToken.length())) + "...");
                        return accessToken;
                    } catch (Exception e) {
                        System.out.println("❌ 로그인 응답 파싱 실패: " + e.getMessage());
                        throw new RuntimeException("Failed to parse login response: " + e.getMessage());
                    }
                })
                .doOnNext(token -> this.jwtToken = token) // JWT 토큰 저장
                .onErrorMap(ex -> {
                    System.out.println("💥 로그인 최종 오류: " + ex.getMessage());
                    return new RuntimeException("Login failed: " + ex.getMessage());
                });
    }
    
    // Refresh Token으로 토큰 재발급
    public Mono<String> refreshAccessToken() {
        if (refreshToken == null || refreshToken.isEmpty()) {
            System.out.println("❌ Refresh Token이 없습니다");
            return Mono.error(new RuntimeException("No refresh token available"));
        }
        
        System.out.println("🔄 Refresh Token으로 토큰 재발급 시도: " + aiServerUrl + "/api/v1/auth/refresh");
        System.out.println("🎫 Refresh Token: " + refreshToken.substring(0, Math.min(20, refreshToken.length())) + "...");
        
        return webClient.post()
                .uri(aiServerUrl + "/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("refresh_token", refreshToken))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .map(errorBody -> {
                                System.out.println("❌ Refresh Token 재발급 실패 - 상태코드: " + clientResponse.statusCode());
                                System.out.println("❌ 에러 응답: " + errorBody);
                                return new RuntimeException("Refresh Token 재발급 실패 [" + clientResponse.statusCode() + "]: " + errorBody);
                            });
                })
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        System.out.println("✅ Refresh Token 응답 수신: " + response.length() + "자");
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = mapper.readTree(response);
                        
                        // 새로운 access_token 추출
                        String newAccessToken = jsonNode.get("access_token").asText();
                        
                        // 새로운 refresh_token 추출 (없으면 기존 값 유지)
                        String newRefreshToken = jsonNode.has("refresh_token") ? 
                            jsonNode.get("refresh_token").asText() : this.refreshToken;
                        
                        // 토큰들 업데이트
                        this.jwtToken = newAccessToken;     // access_token -> jwtToken
                        this.refreshToken = newRefreshToken; // refresh_token -> refreshToken
                        
                        System.out.println("✅ 토큰 재발급 완료 - New Access Token: " + newAccessToken.substring(0, Math.min(20, newAccessToken.length())) + "...");
                        return newAccessToken;
                    } catch (Exception e) {
                        System.out.println("❌ Refresh Token 응답 파싱 실패: " + e.getMessage());
                        throw new RuntimeException("Failed to parse refresh response: " + e.getMessage());
                    }
                })
                .onErrorMap(ex -> {
                    System.out.println("💥 Refresh Token 최종 오류: " + ex.getMessage());
                    return new RuntimeException("Token refresh failed: " + ex.getMessage());
                });
    }
    
    // JWT 토큰으로 자동 로그인
    public Mono<String> ensureAuthenticated() {
        if (jwtToken != null && !jwtToken.isEmpty()) {
            return Mono.just(jwtToken);
        }
        // 환경 변수에서 로그인 정보 사용
        return login(aiUsername, aiPassword, false);
    }

    // 대화 요청(POST) - QueryAsk 사용, JWT 인증 포함, SSE 스트림 반환
    public Flux<String> chatStream(QueryAsk queryAsk) {
        return ensureAuthenticated()
                .flatMapMany(token -> webClient.post()
                        .uri(aiServerUrl + "/api/v1/episode/chat/stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .bodyValue(queryAsk)
                        .retrieve()
                        .bodyToFlux(String.class))
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
                                .flatMapMany(newAccessToken -> {
                                    // 새로운 토큰으로 원래 요청 재시도
                                    return webClient.post()
                                            .uri(aiServerUrl + "/api/v1/episode/chat/stream")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .header("Authorization", "Bearer " + newAccessToken)
                                            .accept(MediaType.TEXT_EVENT_STREAM)
                                            .bodyValue(queryAsk)
                                            .retrieve()
                                            .bodyToFlux(String.class);
                                });
                    }
                    return Flux.error(new RuntimeException("Chat API call failed: " + ex.getMessage()));
                });
    }
    
    // 기존 단일 응답 메서드도 유지 (비스트림 엔드포인트용)
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
