package com.novelbot.api.service.API;

import org.springframework.beans.factory.annotation.Autowired;
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

    // GET 요청
    public Mono<String> getAPI(){
        return webClient.get()
                .uri("http://ec2-52-90-153-224.compute-1.amazonaws.com/")
                .retrieve()
                .bodyToMono(String.class);
    }

    // 로그인 요청(POST)
    public Mono<String> login(String username, String password, boolean rememberMe) {
        return webClient.post()
                .uri("http://ec2-52-90-153-224.compute-1.amazonaws.com/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "username", username,
                        "password", password,
                        "remember_me", rememberMe
                ))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(ex -> new RuntimeException("Login failed: " + ex.getMessage()));
    }

    // 대화 요청(POST)
    public Mono<String> chat(String message, List<Integer> episodeIds) {

        return webClient.post()
                .uri("http://ec2-52-90-153-224.compute-1.amazonaws.com/api/v1/episode/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "message", message,
                        "episode_ids", episodeIds))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(ex -> new RuntimeException("Chat API call failed: " + ex.getMessage()));
    }

    // DELETE 요청 방식
    public Mono<String> deleteAPI(){
        return webClient.delete()
                .uri("http://ec2-52-90-153-224.compute-1.amazonaws.com/", 1)
                .retrieve()
                .bodyToMono(String.class);
    }

    // PATCH 요청 방식
    public Mono<String> patchAPI(){
        return webClient.patch()
                .uri("http://ec2-52-90-153-224.compute-1.amazonaws.com/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("title", "patched title"))
                .retrieve()
                .bodyToMono(String.class);
    }
}
