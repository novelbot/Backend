package com.novelbot.api.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON 파싱 유틸리티 클래스
 */
public class ParseJson {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * AI 서버 스트림 청크에서 텍스트만 추출
     * 
     * @param chunk AI 서버에서 받은 스트림 청크
     * @return 추출된 텍스트 (메시지가 없으면 빈 문자열)
     */
    public static String extractTextFromChunk(String chunk) {
        try {
            StringBuilder result = new StringBuilder();
            
            // 여러 JSON 객체가 연결된 경우를 처리
            String[] jsonParts = splitMultipleJsonObjects(chunk);
            
            for (String jsonPart : jsonParts) {
                if (jsonPart.trim().isEmpty()) continue;
                
                try {
                    JsonNode jsonNode = objectMapper.readTree(jsonPart);
                    
                    // type이 "message"인 경우에만 content 추출
                    if (jsonNode.has("type") && "message".equals(jsonNode.get("type").asText())) {
                        if (jsonNode.has("content")) {
                            result.append(jsonNode.get("content").asText());
                        }
                    }
                    // conversation_info, search_complete 등은 무시
                } catch (Exception e) {
                    // 개별 JSON 파싱 실패 시 해당 부분만 스킵
                    System.out.println("⚠️ JSON 파트 파싱 실패: " + jsonPart.substring(0, Math.min(50, jsonPart.length())) + "...");
                }
            }
            
            return result.toString();
        } catch (Exception e) {
            // 전체 파싱 실패 시 원본 반환
            System.out.println("⚠️ 청크 파싱 완전 실패, 원본 반환: " + e.getMessage());
            return chunk;
        }
    }
    
    /**
     * 연결된 JSON 객체들을 분리
     */
    private static String[] splitMultipleJsonObjects(String chunk) {
        if (!chunk.contains("}{")) {
            // 단일 JSON인 경우
            return new String[]{chunk};
        }
        
        // 여러 JSON이 연결된 경우 분리
        return chunk.split("(?<=})(?=\\{)");
    }
}