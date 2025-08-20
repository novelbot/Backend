package com.novelbot.api.utility;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 스트림 처리 유틸리티 클래스
 */
public class WebSocketStreamHandler {

    /**
     * AI 서버 스트림을 WebSocket으로 실시간 전송
     * 
     * @param streamFlux AI 서버에서 받은 스트림
     * @param queryId 질문 ID
     * @param messagingTemplate WebSocket 메시징 템플릿
     * @return 최종 텍스트 응답
     */
    public static String processStreamToWebSocket(Flux<String> streamFlux, Integer queryId, 
                                                  SimpMessagingTemplate messagingTemplate) {
        StringBuilder fullResponse = new StringBuilder();
        
        streamFlux
            .doOnNext(chunk -> {
                System.out.println("📨 스트림 청크 수신: " + chunk.length() + "자");
                System.out.println("📝 청크 내용: " + chunk);
                
                // JSON에서 텍스트만 추출
                String textContent = ParseJson.extractTextFromChunk(chunk);
                
                // 텍스트가 있는 경우에만 처리
                if (!textContent.isEmpty()) {
                    fullResponse.append(textContent);
                    
                    // 실시간으로 WebSocket을 통해 전송
                    sendStreamChunk(queryId, textContent, messagingTemplate);
                } else {
                    System.out.println("📋 메타데이터 청크 무시: " + chunk.substring(0, Math.min(100, chunk.length())) + "...");
                }
            })
            .doOnComplete(() -> {
                System.out.println("✅ AI 서버 스트림 응답 완료");
                String finalAnswer = fullResponse.toString();
                
                // 스트림 완료 신호 전송
                sendStreamComplete(queryId, finalAnswer, messagingTemplate);
            })
            .doOnError(ex -> {
                System.out.println("❌ AI 서버 스트림 오류: " + ex.getMessage());
                
                // 에러 메시지 전송
                sendStreamError(queryId, "답변 생성 중 오류가 발생했습니다: " + ex.getMessage(), messagingTemplate);
            })
            .subscribe(); // 스트림 구독 시작
            
        return fullResponse.toString();
    }
    
    /**
     * WebSocket으로 스트림 청크 전송
     */
    public static void sendStreamChunk(Integer queryId, String textContent, SimpMessagingTemplate messagingTemplate) {
        try {
            Map<String, Object> streamData = new HashMap<>();
            streamData.put("message", textContent);
            streamData.put("isIncremental", true);
            streamData.put("isComplete", false);
            
            messagingTemplate.convertAndSend("/topic/query/" + queryId, streamData);
            System.out.println("✅ WebSocket 스트림 청크 전송 성공 - 텍스트: " + textContent.length() + "자");
        } catch (Exception wsEx) {
            System.out.println("❌ WebSocket 스트림 청크 전송 실패: " + wsEx.getMessage());
        }
    }
    
    /**
     * WebSocket으로 스트림 완료 신호 전송
     */
    public static void sendStreamComplete(Integer queryId, String finalAnswer, SimpMessagingTemplate messagingTemplate) {
        try {
            Map<String, Object> completeData = new HashMap<>();
            completeData.put("message", finalAnswer);
            completeData.put("isIncremental", false);
            completeData.put("isComplete", true);
            
            messagingTemplate.convertAndSend("/topic/query/" + queryId, completeData);
            System.out.println("✅ WebSocket 스트림 완료 신호 전송 성공 - 최종 답변 길이: " + finalAnswer.length() + "자");
        } catch (Exception wsEx) {
            System.out.println("❌ WebSocket 스트림 완료 신호 전송 실패: " + wsEx.getMessage());
        }
    }
    
    /**
     * WebSocket으로 스트림 에러 메시지 전송
     */
    public static void sendStreamError(Integer queryId, String errorMessage, SimpMessagingTemplate messagingTemplate) {
        try {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", errorMessage);
            errorData.put("isIncremental", false);
            errorData.put("isComplete", true);
            errorData.put("isError", true);
            
            messagingTemplate.convertAndSend("/topic/query/" + queryId, errorData);
            System.out.println("✅ WebSocket 스트림 에러 메시지 전송 성공");
        } catch (Exception wsEx) {
            System.out.println("❌ WebSocket 스트림 에러 메시지 전송 실패: " + wsEx.getMessage());
        }
    }
    
    /**
     * WebSocket으로 일반 에러 메시지 전송
     */
    public static void sendError(Integer queryId, String errorMessage, SimpMessagingTemplate messagingTemplate) {
        try {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("message", errorMessage);
            errorData.put("isIncremental", false);
            errorData.put("isComplete", true);
            errorData.put("isError", true);
            
            messagingTemplate.convertAndSend("/topic/query/" + queryId, errorData);
            System.out.println("✅ WebSocket 예외 에러 메시지 전송 성공");
        } catch (Exception wsEx) {
            System.out.println("❌ WebSocket 예외 에러 메시지 전송 실패: " + wsEx.getMessage());
        }
    }
}