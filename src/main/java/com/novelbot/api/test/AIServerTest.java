package com.novelbot.api.test;

import com.novelbot.api.dto.API.QueryAsk;
import com.novelbot.api.dto.API.QueryAnswerResponse;
import com.novelbot.api.service.API.APIService;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AIServerTest {
    
    public static void main(String[] args) {
        // WebClient 설정
        WebClient webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
        
        // APIService 생성
        APIService apiService = new APIService();
        // Reflection을 사용해서 webClient 설정
        try {
            java.lang.reflect.Field field = APIService.class.getDeclaredField("webClient");
            field.setAccessible(true);
            field.set(apiService, webClient);
        } catch (Exception e) {
            System.out.println("WebClient 설정 실패: " + e.getMessage());
            return;
        }
        
        System.out.println("=== AI 서버 전체 플로우 테스트 시작 ===");
        
        // 1. 로그인 테스트
        System.out.println("1. 로그인 테스트 중...");
        try {
            String token = apiService.login("admin", "admin123", false)
                    .doOnNext(t -> System.out.println("받은 토큰: " + t))
                    .block();
            
            if (token != null && !token.isEmpty()) {
                System.out.println("✅ 로그인 성공!");
            } else {
                System.out.println("❌ 로그인 실패 - 토큰이 없음");
                return;
            }
        } catch (Exception e) {
            System.out.println("❌ 로그인 실패: " + e.getMessage());
            return;
        }
        
        // 2. QueryAsk 생성
        System.out.println("\n2. QueryAsk 객체 생성 중...");
        QueryAsk queryAsk = new QueryAsk();
        queryAsk.setQueryContent("이 소설의 주인공은 누구인가요?");
        queryAsk.setIsBoughtEpisodes(new Integer[]{1, 2, 3});
        // queryAsk.setNovelId(1); // AI 서버에서 지원하지 않음
        queryAsk.setUsingContext(false);
        queryAsk.setBeforeQuery(null);
        
        System.out.println("QueryAsk 생성 완료:");
        System.out.println("- 질문: " + queryAsk.getQueryContent());
        System.out.println("- 에피소드 IDs: " + java.util.Arrays.toString(queryAsk.getIsBoughtEpisodes()));
        // System.out.println("- 소설 ID: " + queryAsk.getNovelId()); // AI 서버에서 지원하지 않음
        
        // 3. AI 서버 채팅 테스트
        System.out.println("\n3. AI 서버 채팅 테스트 중...");
        try {
            QueryAnswerResponse response = apiService.chat(queryAsk)
                    .doOnNext(r -> {
                        System.out.println("AI 서버 응답 받음:");
                        System.out.println("- Conversation ID: " + r.getConversationId());
                        System.out.println("- 답변: " + r.getAnswerContent());
                        System.out.println("- 참조 에피소드: " + java.util.Arrays.toString(r.getReferencedEpisodes()));
                    })
                    .block();
            
            if (response != null) {
                System.out.println("✅ AI 서버 채팅 성공!");
                
                // 4. 응답 검증
                System.out.println("\n4. 응답 데이터 검증 중...");
                boolean isValid = true;
                
                if (response.getAnswerContent() == null || response.getAnswerContent().isEmpty()) {
                    System.out.println("⚠️ 경고: 답변 내용이 비어있음");
                    isValid = false;
                }
                
                if (response.getConversationId() == null || response.getConversationId().isEmpty()) {
                    System.out.println("⚠️ 경고: Conversation ID가 비어있음");
                    isValid = false;
                }
                
                if (response.getReferencedEpisodes() == null) {
                    System.out.println("⚠️ 경고: 참조 에피소드가 null");
                    isValid = false;
                }
                
                if (isValid) {
                    System.out.println("✅ 응답 데이터 검증 통과!");
                } else {
                    System.out.println("⚠️ 응답 데이터에 일부 문제가 있음");
                }
                
                System.out.println("\n=== 전체 플로우 테스트 완료 ===");
                System.out.println("모든 단계가 성공적으로 완료되었습니다!");
                
            } else {
                System.out.println("❌ AI 서버 채팅 실패 - 응답이 없음");
            }
            
        } catch (Exception e) {
            System.out.println("❌ AI 서버 채팅 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}